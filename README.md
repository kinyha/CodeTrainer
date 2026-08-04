# Trainer

Персональный тренажёр Java/Kotlin backend-навыков. Одна и та же задача работает в
двух режимах: набрать эталон пальцами в gittype и написать её с нуля под тесты.

Замысел, формат задач и roadmap — в [DESIGN.md](DESIGN.md). Этот файл — про то,
как этим пользоваться.

---

## Что нужно установить

| Что | Зачем | Проверка |
|---|---|---|
| JDK 21 | сборка | `java -version` |
| [gittype](https://github.com/unhappychoice/gittype) | режим набора | `gittype --version` |
| Python 3 | CLI `bin/train` и `bin/trainer` | `python3 --version` |
| Docker | только тесты с тегом `integration` | `docker ps` |

Gradle ставить не нужно — в репозитории есть wrapper.

---

## Что уже можно делать

В каталоге 45 готовых задач: 6 упражнений вертикального среза, 30 упражнений
первой библиотеки и 9 сценариев L4/L5. Есть algorithms/collections, streams,
Kotlin, concurrency, бизнес-сценарии, SQL на PostgreSQL, Spring и Kafka. Полный
список с тегами, исходниками и разборами — в [docs/CATALOG.md](docs/CATALOG.md).

---

## Режим 1 — набор (drill)

```bash
bin/train streams l2                        # тема + уровень
bin/train streams                           # вся тема
bin/train l3                                # уровень поперёк всех тем
bin/train --topics kafka,springdata --levels 2,3
bin/train --tags groupingBy,tx --lang java  # по тегам из шапки задачи
bin/train due --limit 15                    # подошедшие повторения
bin/train --fresh streams                   # то же, но сбросив кэш gittype
```

Скрипт находит нужный каталог и отдаёт его gittype. Дальше в TUI выбираешь режим:
**Easy/Normal** для разгона на L1–L2, **Hard/Wild** для L4–L5, **Zen** — без
таймера и рангов, когда разбираешь новое.

Что важно знать: **комментарии набирать не нужно**, gittype их пропускает. Поэтому
пояснения в задачах написаны по-русски прямо на строках кода — читаешь их бесплатно,
пока набираешь остальное.

Темы выводятся из `@task`-метаданных. Простой селектор темы запускает gittype на
каталоге напрямую; составной селектор генерирует `.gittypeignore` явными путями.
`TRAINER_DRY_RUN=1 bin/train ...` показывает плейлист, не открывая TUI.

---

## Режим 2 — написать с нуля (recall)

```bash
bin/trainer new streams.l2.AvgSalaryByDepartment
```

В `arena/` появятся заготовка и её тест. В заготовке сохранены сигнатура,
метаданные и Javadoc с контрактом, а тело метода вырезано вместе с подсказками.

```bash
./gradlew :arena:test                                   # должно падать, пока не написал
# ... пишешь реализацию в arena/src/main/... ...
./gradlew :arena:test                                   # зелено?
bin/trainer diff streams.l2.AvgSalaryByDepartment       # сравнить с эталоном
bin/trainer clean                                       # убрать за собой
```

`arena` — черновик: её содержимое не коммитится и перегенерируется в любой момент.
Незаконченная задача там не ломает сборку остальных модулей.

---

## С чего начинать: два пути

Оба рабочие, выбор зависит от того, знаешь ли ты тему.

**Тема новая или подзабытая — начинай с drill.** Набираешь готовое решение и
читаешь пояснения прямо на строках; заодно видишь, что в теме вообще есть.
Разобранный пример на входе работает лучше, чем сидение над пустым методом.

```bash
bin/train streams l1         # просто набираешь и читаешь
bin/train streams l2         # когда l1 стал скучным
```

Дальше, когда рука пошла, бери ту же задачу в recall — уже понимая, что пишешь:

```bash
bin/trainer new streams.l1.ActiveCustomerNames
./gradlew :arena:test
```

**Тему знаешь — начинай с recall.** Сначала пиши сам, смотри, где встал, потом
`diff` с эталоном и разбор, и только потом drill, чтобы правильная форма осела в
пальцах. Так пробелы вскрываются сразу, а не маскируются узнаванием.

Чего делать не стоит: гонять drill по задаче, смысл которой не понял. Это
надёжный способ выучить символы вместо смысла. Если после пары проходов не ясно,
почему решение такое, — открывай `<Task>.md` или иди в recall.

---

## Как устроена задача

```java
// @task streams.l2.AvgSalaryByDepartment      ← ID = путь пакета без trainer.
// @tags groupingBy,averagingDouble,TreeMap    ← по ним потом будет отбор сессий
// @time 15m
// @src  LeetCode-75-Study-Project:...         ← откуда взята
// @doc  AvgSalaryByDepartment.md              ← разбор рядом, если он есть

/**
 * Что вернуть, контракт, критерий «готово».
 */
public static Map<String, Double> calculate(List<Employee> employees) {
    return employees.stream()
            .collect(Collectors.groupingBy(
                    Employee::department,
                    TreeMap::new,                                // WHY: почему так, а не иначе
                    Collectors.averagingDouble(Employee::salary) // EDGE: неочевидный случай
            ));
}
```

Маркеры: `WHY:` — почему выбрано это решение, `EDGE:` — граничный случай,
`COST:` — сложность или цена операции. Рядом может лежать `<Task>.md` с полным
разбором, подводными камнями и вопросами-follow-up с собеседования; для L1–L2 он
обычно не нужен.

Уровень зашит в путь: `streams/l2/` — это L2. Шкала: **L1** рефлекс (5–10 мин),
**L2** идиома API (10–25), **L3** композиция (25–45), **L4** корректность —
ошибка не видна в happy path (45–90), **L5** сценарий из нескольких классов.

---

## Модули и проверки

| Модуль | Что внутри | Тяжесть |
|---|---|---|
| `fixtures` | общие модели и детерминированные данные | — |
| `core-drills` | Java/Kotlin без Spring и Docker | секунды |
| `spring-drills` | Spring Core, MVC, Data | средне |
| `integration-drills` | PostgreSQL и Kafka | тесты с тегом — только по требованию |
| `arena` | черновик recall-режима | — |

Модуль здесь — граница зависимостей, а не тема. Поэтому упражнение на `groupingBy`
не тянет за собой Spring и Docker, и ежедневный прогон занимает секунды.

```bash
./gradlew :core-drills:test                     # ежедневный цикл
./gradlew build                                 # всё, кроме integration и slow
./gradlew :integration-drills:integrationTest   # PostgreSQL, нужен Docker
./gradlew :spring-drills:integrationTest        # JPA/N+1, нужен Docker
./gradlew test --tests '*Streams*'              # точечно
```

Обычная сборка **не** требует Docker: тесты с тегами `integration` и `slow`
исключены по умолчанию. Отдельного флага для этого не нужно.

GitHub Actions запускает два job: быстрый build вместе с regression-тестом CLI,
затем Docker-job со всеми PostgreSQL/Spring integration-тестами.

---

## Добавить свою задачу

1. Выбери модуль по зависимостям и положи файл в `<module>/src/main/java/trainer/<тема>/l<N>/`.
2. Шапка `@task/@tags/@time`, Javadoc с контрактом, пояснения `WHY/EDGE/COST`.
3. Один файл — одна задача, 40–120 строк, класс `final`, методы `static`.
4. Тест — в зеркальном пакете `src/test/...`, имя `<Класс>Test`.
5. Проверь главное: **тест обязан падать на заготовке.** `bin/trainer new <id>` и
   `./gradlew :arena:test` — если зелено сразу, тест ничего не проверяет.
6. Выполни `bin/trainer index` и закоммить обновлённый каталог.

---

## Прогресс и повторения

```bash
bin/trainer stats                    # агрегаты по всем задачам
bin/trainer stats --topic streams --since 30d
bin/trainer due --limit 15           # обновить schedule и показать очередь
bin/train due --limit 15             # сразу открыть очередь в gittype
```

Данные читаются из `~/.gittype/gittype.db`. Состояние SM-2-lite хранится в
`progress/schedule.tsv`: сильный проход растит интервал, слабый возвращает задачу
на завтра. `last_result_id` не даёт повторно применить уже учтённую попытку, а
никогда не встречавшиеся задачи всегда считаются подошедшими.

## Верх пирамиды

Базовая Фаза 4 готова: `SKIP LOCKED`, recursive CTE, exactly-once database
effect, N+1 с Hibernate Statistics, Kafka retry/DLT, transactional outbox,
token bucket, executor backpressure и code-review transfer invariants. Дальше
L4/L5 пополняются по реальным пробелам, а не ради количества.

Порядок работ — в [DESIGN.md](DESIGN.md), §12.
