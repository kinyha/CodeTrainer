#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/../.." && pwd)
TEST_DIR=$(mktemp -d "${TMPDIR:-/tmp}/trainer-progress-test.XXXXXX")
DATABASE="$TEST_DIR/gittype.db"
SCHEDULE="$TEST_DIR/schedule.tsv"
trap 'rm -rf "$TEST_DIR"' EXIT

sqlite3 "$DATABASE" <<'SQL'
CREATE TABLE repositories (
    id INTEGER PRIMARY KEY,
    user_name TEXT NOT NULL,
    repository_name TEXT NOT NULL,
    remote_url TEXT NOT NULL
);
CREATE TABLE challenges (id TEXT PRIMARY KEY, file_path TEXT);
CREATE TABLE stages (id INTEGER PRIMARY KEY, challenge_id TEXT NOT NULL);
CREATE TABLE stage_results (
    id INTEGER PRIMARY KEY,
    stage_id INTEGER NOT NULL,
    repository_id INTEGER NOT NULL,
    wpm REAL,
    accuracy REAL,
    was_skipped BOOLEAN DEFAULT FALSE,
    was_failed BOOLEAN DEFAULT FALSE,
    completed_at DATETIME NOT NULL,
    language TEXT
);
INSERT INTO repositories VALUES (1, 'test', 'CodeTrainer', 'https://github.com/test/CodeTrainer');
INSERT INTO challenges VALUES (
    'challenge-1',
    'core-drills/src/main/java/trainer/streams/l2/AvgSalaryByDepartment.java'
);
INSERT INTO stages VALUES (1, 'challenge-1');
INSERT INTO stage_results VALUES (1, 1, 1, 60.0, 99.0, 0, 0, '2026-08-04 10:00:00', 'java');
SQL

run_due() {
    GITTYPE_DB="$DATABASE" TRAINER_SCHEDULE="$SCHEDULE" TRAINER_TODAY="$1" \
        "$ROOT_DIR/bin/trainer" due --ids --limit 36 >/dev/null
}

task_row() {
    awk -F '\t' '$1 == "streams.l2.AvgSalaryByDepartment" { print; exit }' "$SCHEDULE"
}

assert_row() {
    local expected=$1 actual
    actual=$(task_row)
    if [[ "$actual" != "$expected" ]]; then
        echo "Expected: $expected" >&2
        echo "Actual:   $actual" >&2
        exit 1
    fi
}

run_due 2026-08-04
assert_row $'streams.l2.AvgSalaryByDepartment\t2.60\t1\t1\t2026-08-05\t99.0\t60.0\t2026-08-04 10:00:00\t1'

cp "$SCHEDULE" "$TEST_DIR/first.tsv"
run_due 2026-08-04
cmp "$TEST_DIR/first.tsv" "$SCHEDULE"

sqlite3 "$DATABASE" <<'SQL'
INSERT INTO stages VALUES (2, 'challenge-1');
INSERT INTO stage_results VALUES (2, 2, 1, 40.0, 96.0, 0, 0, '2026-08-05 10:00:00', 'java');
SQL
run_due 2026-08-05
assert_row $'streams.l2.AvgSalaryByDepartment\t2.46\t2\t3\t2026-08-08\t96.0\t40.0\t2026-08-05 10:00:00\t2'

sqlite3 "$DATABASE" <<'SQL'
INSERT INTO stages VALUES (3, 'challenge-1');
INSERT INTO stage_results VALUES (3, 3, 1, 20.0, 90.0, 0, 1, '2026-08-06 10:00:00', 'java');
SQL
run_due 2026-08-06
assert_row $'streams.l2.AvgSalaryByDepartment\t1.92\t0\t1\t2026-08-07\t90.0\t20.0\t2026-08-06 10:00:00\t3'

# Два результата, накопившиеся между запусками, применяются последовательно.
cp "$TEST_DIR/first.tsv" "$SCHEDULE"
run_due 2026-08-06
assert_row $'streams.l2.AvgSalaryByDepartment\t1.92\t0\t1\t2026-08-07\t90.0\t20.0\t2026-08-06 10:00:00\t3'

stats=$(GITTYPE_DB="$DATABASE" "$ROOT_DIR/bin/trainer" stats --topic streams)
grep -q 'streams.l2.AvgSalaryByDepartment' <<< "$stats"
grep -Eq 'AvgSalaryByDepartment +3 +40\.0 +95\.0 +90\.0' <<< "$stats"

echo "progress tooling test passed"
