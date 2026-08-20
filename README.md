# CodeTrainer — как пользоваться

Это мой локальный тренажёр Java/Kotlin backend. Задачи можно проходить
тремя способами:

| Что хочу сделать | Команда |
|---|---|
| Набрать только сигнатуру и тело решения | `bin/focus streams` |
| Потренировать задачу в полном исходнике | `bin/train streams` |
| Написать решение с нуля и прогнать тесты | `bin/trainer new streams.l2.AvgSalaryByDepartment` |

Полный список всех 108 задач с ID, тегами, исходниками и разборами:
[docs/CATALOG.md](docs/CATALOG.md).

## Быстрый старт

Все команды ниже вызываются из корня репозитория:

```bash
cd /path/to/CodeTrainer
```

Для обычных задач нужны JDK 21 и Python 3. Для режимов `focus` и `train`
дополнительно нужен [gittype](https://github.com/unhappychoice/gittype).
Docker нужен только для PostgreSQL integration-тестов.

```bash
java -version
python3 --version
gittype --version
docker info             # нужен не каждый день
```

Gradle отдельно ставить не нужно: используется `./gradlew` из репозитория.

Первая сессия:

```bash
bin/focus --dry-run streams l1  # посмотреть плейлист
bin/focus streams l1            # запустить gittype
```

В меню gittype для `focus` удобнее выбирать **Zen** или **Wild**: так решение
не режется на слишком мелкие куски.

## Как выбирать задачи

У каждой задачи есть ID:

```text
streams.l2.AvgSalaryByDepartment
└─тема─┘ └уровень┘ └──────имя задачи─────┘
```

ID нужен для `bin/trainer new` и `bin/trainer diff`. Для `focus` и `train`
можно выбирать целую тему или уровень.

Шкала уровней:

| Уровень | Что он означает | Обычное время |
|---|---|---:|
| L1 | Базовый рефлекс, один приём | 5–10 мин |
| L2 | Идиома API, несколько шагов | 10–25 мин |
| L3 | Композиция приёмов | 25–45 мин |
| L4 | Корректность, транзакции, edge cases | 45–90 мин |
| L5 | Сценарий из нескольких классов/компонентов | 60+ мин |

L1–L5 в пути задачи и Easy/Normal/Hard/Zen/Wild в gittype — это разные вещи.
Первое описывает сложность самой задачи, второе — режим набора.

## Где лежат задачи

| Каталог | Что там | Сколько |
|---|---|---:|
| `core-drills/src/main/java/trainer/` | Algorithms, Collections, Streams, Concurrency, Patterns | 83 |
| `core-drills/src/main/kotlin/trainer/kotlinlang/` | Kotlin language | 6 |
| `spring-drills/src/main/java/trainer/` | Spring Core, Data, Web | 7 |
| `integration-drills/src/main/java/trainer/` | SQL/PostgreSQL и Kafka | 12 |
| `*/src/test/...` | Тесты в том же пакете | — |
| `docs/CATALOG.md` | Кликабельный каталог всех задач | 108 |

Внутри каталога путь всегда имеет вид:

```text
<module>/src/main/<java|kotlin>/trainer/<topic>/l<level>/<Task>.<java|kt>
```

Например:

```text
core-drills/src/main/java/trainer/streams/l2/AvgSalaryByDepartment.java
core-drills/src/test/java/trainer/streams/l2/AvgSalaryByDepartmentTest.java
core-drills/src/main/java/trainer/streams/l2/AvgSalaryByDepartment.md
```

`.md` рядом с исходником — это разбор решения. Он есть не у каждой задачи;
ссылки на все имеющиеся разборы есть в каталоге.

### Темы, которые уже есть

| Тема-селектор | Уровни | Задач | Что внутри |
|---|---|---:|---|
| `algorithms` | L1–L4 | 20 | Массивы/строки, two pointers, sliding window, binary search, стек, связный список, LRU-кэш, графы |
| `collections` | L1–L4 | 20 | List/Set/Map, Comparator, TreeMap, PriorityQueue, Deque, LinkedHashMap, equals/hashCode |
| `streams` | L1–L4 | 20 | filter/map, collectors, grouping, `flatMap`, teeing, свой `Collector` |
| `kotlinlang` | L1–L3 | 6 | Null safety, sequences, sealed, reified |
| `concurrency` | L1–L5 | 20 | Thread/join, synchronized, Atomic/CAS, Lock/Condition, wait/notify, backpressure, rate limiter |
| `patterns` | L3–L4 | 3 | Business rules, idempotency, transfer invariants |
| `springcore` | L2 | 1 | Constructor injection, service boundary |
| `springdata` | L3–L5 | 3 | Transactions, N+1, transactional outbox |
| `springweb` | L2–L3 | 3 | ResponseEntity, validation, ProblemDetail |
| `kafka` | L2–L4 | 4 | Keys, manual ack, tombstone, retry/DLT |
| `sql` | L1–L5 | 8 | JOIN/GROUP BY, window/CTE, locking, idempotency |

## Режим `focus`: набирать только решение

Это основной режим для короткой механической практики. `bin/focus` извлекает
сигнатуру и тело эталонного решения. Package, imports, конструкторы и прочая
обвязка в тренировочный файл не попадают.

```bash
bin/focus streams                 # все 6 streams-задач
bin/focus streams l2              # только streams L2
bin/focus l3                      # все L3 из всех тем
bin/focus --lang kotlin l2        # все Kotlin L2
bin/focus --dry-run streams l2    # показать файлы, не запускать TUI
```

Перед каждым запуском скрипт заново создаёт зеркало в:

```text
~/.local/share/trainer-drill
```

Этот каталог не нужно редактировать: он временный. Путь можно переопределить
через `TRAINER_DRILL_DIR`, но не стоит класть его в каталог с именем `.cache`, `cache`
или `build`: gittype игнорирует такие пути.

Важно: сессии `focus` идут из отдельного зеркала и пока не попадают в
`bin/trainer stats` и расписание `due`. Для учёта прогресса используй `bin/train`.

## Режим `train`: задача в полном исходнике

`bin/train` передаёт gittype реальные файлы из репозитория. Используй этот режим,
когда нужен контекст класса или когда хочешь, чтобы проход учитывался в статистике.

```bash
bin/train streams                         # вся тема
bin/train streams l2                      # тема + уровень
bin/train l3                              # весь L3
bin/train --topics kafka,springdata       # несколько тем
bin/train --topics kafka,sql --levels 2,3 # темы + уровни
bin/train --tags groupingBy,transactional # хотя бы один из тегов
bin/train --lang kotlin --levels 1,2      # только Kotlin
bin/train due --limit 15                  # очередь повторений
bin/train --fresh streams                 # очистить cache челленджей gittype
```

Чтобы только увидеть итоговый плейлист:

```bash
TRAINER_DRY_RUN=1 bin/train --topics streams,collections --levels 2,3
```

При сложном фильтре `bin/train` временно генерирует `.gittypeignore` в корне.
Файл не нужно редактировать или коммитить.

## Режим `recall`: написать с нуля

Выбери ID в [каталоге](docs/CATALOG.md) и создай заготовку:

```bash
bin/trainer new streams.l2.AvgSalaryByDepartment
```

Команда скопирует задачу и её тест в `arena/src/`, а эталонное тело решения
заменит на `TODO`. Открой созданный файл в IDE и пиши решение.

Полный цикл:

```bash
bin/trainer new streams.l2.AvgSalaryByDepartment
./gradlew :arena:test

# редактирую arena/src/main/java/trainer/streams/l2/AvgSalaryByDepartment.java

./gradlew :arena:test
bin/trainer diff streams.l2.AvgSalaryByDepartment
bin/trainer clean
```

`diff` показывает разницу между текущим кодом в `arena` и эталоном. `clean`
удаляет все сгенерированные исходники из `arena/src/`. Они игнорируются Git и не ломают
остальную сборку.

Для SQL-задач и `springdata.l4.NPlusOneFetchJoin` тест в `arena` потребует
запущенный Docker.

## Как читать исходник задачи

В начале файла лежат метаданные:

```java
// @task streams.l2.AvgSalaryByDepartment
// @tags groupingBy,averagingDouble,TreeMap
// @time 15m
// @doc AvgSalaryByDepartment.md
```

| Маркер | Что означает |
|---|---|
| `@task` | Точный ID задачи |
| `@tags` | Ключевые техники для фильтра `bin/train --tags` |
| `@time` | Ориентир на recall-попытку |
| `@doc` | Разбор рядом с задачей |

В коде встречаются короткие пометки:

- `WHY:` — почему выбран такой подход;
- `EDGE:` — неочевидный граничный случай;
- `COST:` — сложность или цена операции.

gittype пропускает комментарии: их можно читать, но не нужно набирать.

## Как запускать тесты

Быстрые проверки без Docker:

```bash
./gradlew :core-drills:test
./gradlew :spring-drills:test
./gradlew :integration-drills:test
./gradlew build
```

`build` и обычные `test` исключают тесты с тегами `integration` и `slow`.
Kafka-тесты используют embedded Kafka и Docker не требуют.

Прогон одного теста:

```bash
./gradlew :core-drills:test \
  --tests 'trainer.streams.l2.AvgSalaryByDepartmentTest'
```

PostgreSQL integration-тесты через Testcontainers:

```bash
docker info
./gradlew :integration-drills:integrationTest
./gradlew :spring-drills:integrationTest
```

При первом запуске Testcontainers скачает PostgreSQL image, поэтому первый прогон
будет дольше обычного.

## Прогресс и повторения

gittype хранит результаты в `~/.gittype/gittype.db`. CodeTrainer читает оттуда проходы
реальных файлов, запущенных через `bin/train`.

```bash
bin/trainer stats
bin/trainer stats --topic streams
bin/trainer stats --topic streams --since 30d

bin/trainer due --limit 15
bin/trainer due --limit 15 --ids
bin/train due --limit 15
```

Расписание повторений хранится в `progress/schedule.tsv`. Если задача ни разу
не проходилась, она считается готовой к повторению. Плохой проход возвращает
её в очередь на завтра, уверенный проход увеличивает интервал.

## Готовые сценарии

### 15 минут: размять пальцы

```bash
bin/focus streams l1
```

### 30 минут: разобрать и повторить тему

```bash
bin/train collections l2
bin/trainer new collections.l2.ComparatorChain
./gradlew :arena:test
```

### 60+ минут: recall без подсказки

```bash
bin/trainer new patterns.l4.IdempotentPaymentService
./gradlew :arena:test
bin/trainer diff patterns.l4.IdempotentPaymentService
bin/trainer clean
```

### Очередь повторений

```bash
bin/trainer due --limit 10
bin/train due --limit 10
```

## Если что-то не работает

### `bin/focus` не показывает файлы

```bash
bin/focus --dry-run streams
find ~/.local/share/trainer-drill -type f
```

Не задавай `TRAINER_DRILL_DIR` внутри `.cache`, `cache` или `build`. Если переменная уже
задана, на один запуск можно вернуть безопасный путь:

```bash
TRAINER_DRILL_DIR="$HOME/.local/share/trainer-drill" bin/focus streams
```

### gittype пишет `cursor position could not be read`

Запускай `bin/focus` и `bin/train` в обычном интерактивном Terminal, а не в
IDE Run/Output panel: TUI нужен настоящий TTY.

### `Arena already contains ...`

```bash
bin/trainer clean
bin/trainer new <task-id>
```

### Integration-тест не видит Docker

```bash
docker info
./gradlew :integration-drills:integrationTest --info
```

## Служебные команды

```bash
bin/focus --help
bin/train --help
bin/trainer --help
bin/trainer index       # пересобрать docs/CATALOG.md
```

Как устроен формат задач и как дальше развивать тренажёр, описано в [DESIGN.md](DESIGN.md).
