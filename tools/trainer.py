#!/usr/bin/env python3
from __future__ import annotations

import argparse
import csv
import difflib
import os
import re
import shutil
import sqlite3
import subprocess
import sys
import tempfile
from collections import defaultdict
from datetime import date, datetime, timedelta
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SOURCE_ROOTS = tuple(ROOT / module / "src/main" for module in
                     ("core-drills", "spring-drills", "integration-drills"))
REPO_FILTER = """(LOWER(r.repository_name) IN ('trainer', 'codetrainer')
                    OR LOWER(r.remote_url) LIKE '%/codetrainer%')"""
SCHEDULE_FIELDS = ("task_id", "ease", "reps", "interval_days", "due",
                   "last_acc", "last_wpm", "last_seen", "last_result_id")


def fail(message: str, code: int = 2) -> None:
    print(message, file=sys.stderr)
    raise SystemExit(code)


def metadata(path: Path) -> dict[str, str]:
    result: dict[str, str] = {}
    for line in path.read_text().splitlines():
        match = re.match(r"// @(task|tags|time|src|doc)\s+(.*)", line)
        if match:
            result[match.group(1)] = match.group(2).strip()
    return result


def exercises() -> list[dict[str, object]]:
    result = []
    for source_root in SOURCE_ROOTS:
        if not source_root.exists():
            continue
        for path in sorted((*source_root.rglob("*.java"), *source_root.rglob("*.kt"))):
            meta = metadata(path)
            if "task" not in meta:
                continue
            task_id = meta["task"]
            result.append({"id": task_id, "path": path, "relative": path.relative_to(ROOT),
                           "topic": task_id.split(".")[0], "level": task_id.split(".")[1][1:],
                           "language": "kotlin" if path.suffix == ".kt" else "java", "meta": meta})
    return sorted(result, key=lambda item: str(item["path"]))


def csv_values(value: str | None) -> set[str]:
    return {part.strip().lower().removeprefix("l") for part in (value or "").split(",") if part.strip()}


def database(required: bool) -> sqlite3.Connection | None:
    path = Path(os.environ.get("GITTYPE_DB", Path.home() / ".gittype/gittype.db"))
    if not path.is_file():
        if required:
            fail(f"gittype database is missing: {path}", 1)
        return None
    connection = sqlite3.connect(f"file:{path}?mode=ro", uri=True)
    connection.row_factory = sqlite3.Row
    return connection


def load_schedule(path: Path) -> dict[str, dict[str, str]]:
    if not path.is_file():
        return {}
    with path.open(newline="") as stream:
        return {row["task_id"]: row for row in csv.DictReader(stream, delimiter="\t")}


def update_schedule() -> tuple[Path, list[dict[str, str]]]:
    schedule = Path(os.environ.get("TRAINER_SCHEDULE", ROOT / "progress/schedule.tsv"))
    today = date.fromisoformat(os.environ.get("TRAINER_TODAY", date.today().isoformat()))
    old = load_schedule(schedule)
    baselines: dict[str, float] = {}
    results: dict[str, list[sqlite3.Row]] = defaultdict(list)
    connection = database(False)
    if connection:
        for row in connection.execute(f"""SELECT sr.language, AVG(sr.wpm) AS baseline
            FROM stage_results sr JOIN repositories r ON r.id=sr.repository_id
            WHERE {REPO_FILTER} AND sr.wpm IS NOT NULL GROUP BY sr.language"""):
            baselines[row["language"]] = row["baseline"]
        query = f"""SELECT c.file_path, sr.id, sr.accuracy, sr.wpm, sr.was_failed,
            sr.was_skipped, sr.completed_at, sr.language
            FROM stage_results sr JOIN stages s ON s.id=sr.stage_id
            JOIN challenges c ON c.id=s.challenge_id
            JOIN repositories r ON r.id=sr.repository_id WHERE {REPO_FILTER}
            ORDER BY sr.completed_at, sr.id"""
        for row in connection.execute(query):
            results[row["file_path"].removeprefix("./")].append(row)
        connection.close()

    rows = []
    for task in exercises():
        task_id, relative = str(task["id"]), str(task["relative"])
        state = dict(old.get(task_id, {}))
        state.update({key: state.get(key, default) for key, default in
                      (("task_id", task_id), ("ease", "2.50"), ("reps", "0"),
                       ("interval_days", "0"), ("due", today.isoformat()),
                       ("last_acc", "-"), ("last_wpm", "-"), ("last_seen", "-"),
                       ("last_result_id", "0"))})
        ease, reps = float(state["ease"]), int(state["reps"])
        interval, last_id = int(state["interval_days"]), int(state["last_result_id"])
        for result in results.get(relative, []):
            if result["id"] <= last_id:
                continue
            accuracy, wpm = float(result["accuracy"] or 0), float(result["wpm"] or 0)
            baseline = baselines.get(result["language"], wpm)
            if result["was_failed"] or result["was_skipped"] or accuracy < 95:
                quality = 1
            elif accuracy >= 98 and wpm >= .9 * baseline:
                quality = 5
            else:
                quality = 3
            delta = 5 - quality
            ease = min(2.8, max(1.3, ease + .1 - delta * (.08 + delta * .02)))
            if quality == 1:
                reps, interval = 0, 1
            else:
                reps += 1
                interval = 1 if reps == 1 else 3 if reps == 2 else max(1, round(interval * ease))
            completed = datetime.fromisoformat(result["completed_at"])
            state.update(due=(completed.date() + timedelta(days=interval)).isoformat(),
                         last_acc=f"{accuracy:.1f}", last_wpm=f"{wpm:.1f}",
                         last_seen=result["completed_at"], last_result_id=str(result["id"]))
            last_id = result["id"]
        state.update(ease=f"{ease:.2f}", reps=str(reps), interval_days=str(interval))
        rows.append(state)
    schedule.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.NamedTemporaryFile("w", dir=schedule.parent, delete=False, newline="") as stream:
        writer = csv.DictWriter(stream, SCHEDULE_FIELDS, delimiter="\t", lineterminator="\n")
        writer.writeheader()
        writer.writerows(rows)
        temporary = Path(stream.name)
    temporary.replace(schedule)
    return schedule, rows


def due_tasks(limit: int) -> list[dict[str, str]]:
    today = os.environ.get("TRAINER_TODAY", date.today().isoformat())
    _, rows = update_schedule()
    return sorted((row for row in rows if row["due"] <= today),
                  key=lambda row: (row["due"], row["task_id"]))[:limit]


def train_command(arguments: list[str]) -> None:
    parser = argparse.ArgumentParser(prog="bin/train")
    parser.add_argument("selectors", nargs="*")
    parser.add_argument("--fresh", action="store_true")
    parser.add_argument("--topics"); parser.add_argument("--levels")
    parser.add_argument("--tags"); parser.add_argument("--lang", choices=("java", "kotlin"))
    parser.add_argument("--limit", type=int, default=15)
    args = parser.parse_args(arguments)
    if args.limit < 1 or len(args.selectors) > 2:
        parser.error("invalid selector or limit")
    topics, levels, tags = csv_values(args.topics), csv_values(args.levels), csv_values(args.tags)
    due_mode = args.selectors == ["due"]
    if args.selectors and not due_mode:
        first = args.selectors[0]
        if re.fullmatch(r"l[1-5]", first):
            levels.add(first[1:])
        else:
            topics.add(first.lower())
        if len(args.selectors) == 2:
            if not re.fullmatch(r"l[1-5]", args.selectors[1]): parser.error("invalid level")
            levels.add(args.selectors[1][1:])
    if not (topics or levels or tags or args.lang or due_mode): parser.error("selector is required")
    if levels - set("12345"): parser.error("levels must be between 1 and 5")
    due_ids = {row["task_id"] for row in due_tasks(args.limit)} if due_mode else set()
    selected = [item for item in exercises()
                if (not topics or item["topic"].lower() in topics)
                and (not levels or item["level"] in levels)
                and (not args.lang or item["language"] == args.lang)
                and (not tags or tags & csv_values(item["meta"].get("tags")))
                and (not due_mode or item["id"] in due_ids)]
    if not selected: fail("No exercises match the selector", 1)
    languages = ",".join(sorted({str(item["language"]) for item in selected}))
    show = lambda values: ",".join(sorted(values)) or "*"
    selector = f"due;limit={args.limit}" if due_mode else \
        f"topics={show(topics)};levels={show(levels)};tags={show(tags)};lang={args.lang or '*'}"
    direct = not due_mode and len(topics) == 1 and len(levels) <= 1 and not tags and not args.lang
    ignore = ROOT / ".gittypeignore"
    if direct:
        ignore.unlink(missing_ok=True)
        first = selected[0]; module = Path(first["relative"]).parts[0]
        target = Path(module) / "src/main" / str(first["language"]) / "trainer" / next(iter(topics))
        if levels: target /= "l" + next(iter(levels))
    else:
        selected_paths = {item["relative"] for item in selected}
        lines = [f"# GENERATED by bin/train — selector: {selector}", "# Do not edit, do not commit.",
                 "/arena/", "/fixtures/", "/docs/", "/*/src/test/"]
        lines += ["/" + str(item["relative"]) for item in exercises() if item["relative"] not in selected_paths]
        ignore.write_text("\n".join(lines) + "\n")
        target = Path(".")
    print(f"Playlist: {selector} ({len(selected)} file(s), {languages})")
    for item in selected: print(f"  {item['id']}")
    if os.environ.get("TRAINER_DRY_RUN") == "1": return
    if not shutil.which("gittype"): fail("gittype is not installed", 127)
    if args.fresh: subprocess.run(("gittype", "cache", "clear"), check=True)
    os.chdir(ROOT); os.execvp("gittype", ("gittype", str(target), "--langs", languages))


def find_task(task_id: str) -> dict[str, object]:
    matches = [item for item in exercises() if item["id"] == task_id]
    if len(matches) != 1: fail(f"Unknown or non-unique task: {task_id}", 1)
    return matches[0]


def task_paths(task: dict[str, object]) -> tuple[Path, Path, Path]:
    source = Path(task["path"]); module = ROOT / Path(task["relative"]).parts[0]
    source_root = module / "src/main"; relative = source.relative_to(source_root)
    test = module / "src/test" / relative.with_name(relative.stem + "Test" + relative.suffix)
    return module, relative, test


def strip_solution(source: Path) -> str:
    output, skipping, found = [], False, 0
    for line in source.read_text().splitlines(keepends=True):
        if "// ---8<--- solution" in line:
            found += 1; skipping = True; indent = line[:len(line) - len(line.lstrip())]
            output.append(indent + ('TODO("implement")' if source.suffix == ".kt"
                                    else 'throw new UnsupportedOperationException("TODO: implement");') + "\n")
        elif "// --->8--- solution" in line:
            if not skipping: fail(f"Invalid solution markers in {source}", 1)
            skipping = False
        elif not skipping: output.append(line)
    if skipping or not found: fail(f"Invalid or missing solution markers in {source}", 1)
    return "".join(output)


def new_task(task_id: str) -> None:
    task = find_task(task_id); module, relative, test = task_paths(task)
    arena_source, arena_test = ROOT / "arena/src/main" / relative, ROOT / "arena/src/test" / test.relative_to(module / "src/test")
    if not test.is_file(): fail(f"Reference test is missing: {test}", 1)
    if arena_source.exists() or arena_test.exists(): fail(f"Arena already contains {task_id}; clean it first", 1)
    arena_source.parent.mkdir(parents=True, exist_ok=True); arena_source.write_text(strip_solution(Path(task["path"])))
    arena_test.parent.mkdir(parents=True, exist_ok=True)
    for support in test.parent.glob(test.stem.removesuffix("Test") + "*Test" + test.suffix): shutil.copy2(support, arena_test.parent)
    for support in (module / "src/test").rglob("*TestSupport.java"):
        destination = ROOT / "arena/src/test" / support.relative_to(module / "src/test")
        destination.parent.mkdir(parents=True, exist_ok=True); shutil.copy2(support, destination)
    resources = module / "src/test/resources"
    if resources.is_dir(): shutil.copytree(resources, ROOT / "arena/src/test/resources", dirs_exist_ok=True)
    print(f"Created recall exercise:\n  {arena_source}\n  {arena_test}\nRun: ./gradlew :arena:test")


def diff_task(task_id: str) -> None:
    task = find_task(task_id); _, relative, _ = task_paths(task)
    arena = ROOT / "arena/src/main" / relative
    if not arena.is_file(): fail(f"Task is not present in arena: {task_id}", 1)
    print("".join(difflib.unified_diff(arena.read_text().splitlines(True), Path(task["path"]).read_text().splitlines(True),
                                       f"arena/{task_id}", f"reference/{task_id}")), end="")


def generate_index() -> None:
    lines = ["# Каталог упражнений", "", "<!-- GENERATED by bin/trainer index. Do not edit manually. -->", "",
             "| ID | Теги | Recall | Исходник | Разбор |", "|---|---|---:|---|---|"]
    for item in exercises():
        meta, relative = item["meta"], item["relative"]
        doc = Path(relative).with_name(meta["doc"]) if "doc" in meta else None
        lines.append(f"| `{item['id']}` | {meta.get('tags', '-').replace(',', ', ')} | {meta.get('time', '-')} | "
                     f"[{relative}](../{relative}) | {f'[{doc.name}](../{doc})' if doc else '—'} |")
    destination = ROOT / "docs/CATALOG.md"; destination.parent.mkdir(exist_ok=True)
    destination.write_text("\n".join(lines) + "\n"); print(f"Generated {destination}")


def stats_command(topic: str | None, since: str | None) -> None:
    connection = database(True); parameters: list[object] = []; date_filter = ""
    if since:
        if not re.fullmatch(r"[0-9]+d", since): fail("--since must look like 30d")
        date_filter = "AND sr.completed_at >= datetime('now', ?)"; parameters.append(f"-{since[:-1]} days")
    query = f"""SELECT c.file_path, COUNT(*) runs, ROUND(AVG(sr.wpm),1) avg_wpm,
        ROUND(AVG(sr.accuracy),1) avg_acc, ROUND(MIN(sr.accuracy),1) worst_acc,
        MAX(sr.completed_at) last_seen, CAST(julianday('now')-julianday(MAX(sr.completed_at)) AS INT) days_ago,
        SUM(sr.was_failed) failed, SUM(sr.was_skipped) skipped
        FROM stage_results sr JOIN stages s ON s.id=sr.stage_id JOIN challenges c ON c.id=s.challenge_id
        JOIN repositories r ON r.id=sr.repository_id WHERE {REPO_FILTER} {date_filter}
        GROUP BY c.file_path ORDER BY MAX(sr.completed_at) DESC"""
    task_by_path = {str(item["relative"]): item["id"] for item in exercises()}
    print(f"{'TASK':48} {'RUNS':>5} {'AVG_WPM':>8} {'AVG_ACC':>8} {'WORST_ACC':>9} {'LAST_SEEN':>10} {'FAILED':>6} {'SKIPPED':>7}")
    printed = 0
    for row in connection.execute(query, parameters):
        task_id = task_by_path.get(row["file_path"].removeprefix("./"))
        if not task_id or topic and not task_id.startswith(topic + "."): continue
        avg_wpm, avg_acc = row["avg_wpm"] if row["avg_wpm"] is not None else "-", row["avg_acc"] if row["avg_acc"] is not None else "-"
        worst_acc = row["worst_acc"] if row["worst_acc"] is not None else "-"
        print(f"{task_id:48} {row['runs']:5} {avg_wpm!s:>8} {avg_acc!s:>8} {worst_acc!s:>9} "
              f"{str(row['days_ago'])+'d':>10} {row['failed']:6} {row['skipped']:7}"); printed += 1
    connection.close()
    if not printed: print("No gittype results for CodeTrainer yet.")


def trainer_command(arguments: list[str]) -> None:
    parser = argparse.ArgumentParser(prog="bin/trainer"); commands = parser.add_subparsers(dest="command", required=True)
    for name in ("new", "diff"):
        command = commands.add_parser(name); command.add_argument("task_id")
    commands.add_parser("clean"); commands.add_parser("index")
    stats = commands.add_parser("stats"); stats.add_argument("--topic"); stats.add_argument("--since")
    due = commands.add_parser("due"); due.add_argument("--limit", type=int, default=15); due.add_argument("--ids", action="store_true")
    args = parser.parse_args(arguments)
    if args.command == "new": new_task(args.task_id)
    elif args.command == "diff": diff_task(args.task_id)
    elif args.command == "clean":
        target = ROOT / "arena/src"
        if target.exists(): shutil.rmtree(target); print("Removed generated arena sources")
        else: print("Arena is already empty")
    elif args.command == "index": generate_index()
    elif args.command == "stats": stats_command(args.topic, args.since)
    elif args.command == "due":
        if args.limit < 1: parser.error("--limit must be positive")
        rows = due_tasks(args.limit)
        if not args.ids: print(f"{'TASK':48} {'DUE':>10} {'REPS':>5} {'EASE':>8}")
        for row in rows:
            print(row["task_id"] if args.ids else
                  f"{row['task_id']:48} {row['due']:>10} {row['reps']:>5} {row['ease']:>8}")


if __name__ == "__main__":
    if len(sys.argv) < 2 or sys.argv[1] not in ("train", "trainer"): fail("internal mode is required")
    (train_command if sys.argv[1] == "train" else trainer_command)(sys.argv[2:])
