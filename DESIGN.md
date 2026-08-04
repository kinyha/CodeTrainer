# Trainer — дизайн-документ

Статус: draft 1.0 (сведён из четырёх независимых вариантов)
Дата: 2026-08-04

---

## 1. Зачем

Персональный тренажёр для Java/Kotlin backend-разработчика уровня Middle+.
Доводит до автоматизма код, который приходится писать каждый день, и одновременно
напоминает, **почему** он написан именно так.

Два режима на одном и том же контенте:

1. **Drill (typing)** — набрать готовый проверенный код в [gittype](https://github.com/unhappychoice/gittype).
   Цель — убрать паузу между «знаю, что писать» и «написал».
2. **Recall (руками)** — получить заготовку с сигнатурами, написать реализацию
   с нуля, проверить тестами.

Единица контента — не статья и не учебное приложение, а короткое исполняемое
упражнение на один навык.

### Не-цели

- энциклопедия всей Java/Spring-экосистемы;
- олимпиадные трюки;
- один большой production-like сервис, который надо поднимать ради пятиминутного упражнения;
- оценка только по скорости печати;
- насильно уместить любой Spring/Kafka-сценарий в один файл;
- перенести всё старое без ревизии и тестов.

### Метрики

Базовая линия взята из твоих реальных сессий в `~/.gittype/gittype.db`, а не с потолка.

| Метрика | Источник | Сейчас | Цель |
|---|---|---|---|
| WPM (java) | `session_results.wpm` | 26.5 – 34.8 | 45+ |
| Accuracy | `session_results.accuracy` | 82.4 – 89.2 % | 95 %+ на L1–L3 |
| Покрытие drill | доля задач с ≥3 успешными проходами | — | 80 % каталога |
| Покрытие recall | доля задач, решённых с нуля с зелёным тестом | — | 60 % каталога |
| Удержание | падение WPM при повторе через 14 дней | — | ≤ 15 % |
| Ритм | — | — | 5 × 20 мин drill + 2 задачи recall в неделю |

Первые две метрики gittype считает сам, остальные — `bin/trainer stats` (§9).

---

## 2. Механика gittype: что проверено и что из этого следует

Версия 0.10.1 (brew). Всё ниже — проверено на CLI и на реальных данных в
`~/.gittype/gittype.db`, а не взято из README.

| Факт | Следствие для дизайна |
|---|---|
| CLI: `gittype [PATH] [--langs …] [--repo …]` + `history/stats/export/cache/repo/trending`. **Опции `--config` нет.** | Дизайн на неё не опирается |
| Нельзя выбрать конкретный файл — gittype сам случайно берёт куски из указанного пути | **Директория = плейлист.** Дерево проектируется под это |
| Рычагов фильтрации ровно два: путь и `.gittypeignore` (синтаксис .gitignore) | Произвольные срезы — через генерируемый ignore (§3.4) |
| Языки: java, kotlin, rust, python, go, … — **`sql` не поддерживается** | SQL тренируется как Java text block (§6) |
| Размер куска не фиксирован. В твоей БД режим Wild дал куски от 25 символов (одна строка) до 3752 (весь файл, строки 1–92) | «Целый файл» — не переключатель, а следствие того, что файл маленький |
| `package` и `import` входят в текст челленджа (проверено на `Quote.java`, code_content начинается с `package`) | Короткие пакеты, минимум импортов |
| `challenges.file_path` — путь **относительно корня репозитория** | Один файл = одна задача даёт аналитику по задачам бесплатно. Любой подход, который копирует файлы во временный репо, ломает эту связь — отвергнут |
| **Комментарии не набираются** — gittype их пропускает (проверено в живой сессии; в БД они размечены в `challenges.comment_ranges`) | **Пояснения бесплатны.** Русский текст можно ставить прямо на строке кода: он виден во время набора и не стоит ни одного нажатия. Это определяет весь формат задачи (§6) |
| Кэш кусков в `~/.gittype/cache`, чистится `gittype cache clear` | После правки эталонов первая сессия — с `--fresh` |
| Схема БД (проверена): `challenges`, `stages`, `stage_results(stage_id, wpm, accuracy, was_skipped, was_failed, language, …)`, `session_results`, `sessions`, `repositories` | Прогресс и интервальные повторения строятся на sqlite напрямую (§9) |

Твои сессии в БД — все в режиме Wild, по 3 стадии. Это уже привычный режим,
дизайн под него и затачивается.

### Две разные шкалы сложности

Главный источник путаницы, фиксируем сразу:

| Шкала | Кто задаёт | Что значит |
|---|---|---|
| **Уровень 1–5** | мы, в метаданных задачи | когнитивная сложность: что надо знать и продумать |
| **Easy / Normal / Hard / Wild / Zen** | gittype, выбирается в TUI | объём куска текста; Zen — без таймера и рангов |

Они ортогональны. Рекомендуемые сочетания: L1–L2 → Easy/Normal (разгон,
много повторов), L3 → Normal/Hard, L4–L5 → Hard/Wild (тренируется структура
решения, а не только пальцы), любой уровень → Zen для разбора нового.

---

## 3. Структура

### 3.1 Модуль = граница зависимостей, а не тема

Ключевое решение. Упражнение на `groupingBy` не должно тянуть Spring, Kafka и
Docker. Темы и уровни выражаются **пакетами**, а не модулями — иначе получится
три десятка микромодулей Gradle.

```
:fixtures            модели и датасеты, без зависимостей
:core-drills         java + kotlin: algorithms, collections, streams,
                     concurrency, kotlinlang, coroutines, patterns, testing
                     → зависимости: fixtures, junit, assertj. Ни Spring, ни Docker
:spring-drills       springcore, springweb, springdata  → + Spring Boot BOM
:integration-drills  sql, kafka                          → + Testcontainers, spring-kafka
:arena               рабочее место режима recall, содержимое gitignored
```

`./gradlew :core-drills:test` — секунды, без сети и Docker. Это ежедневный
feedback loop; всё тяжёлое изолировано.

### 3.2 Дерево

```
trainer/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle/libs.versions.toml          все версии здесь
├── .gitignore                          включает .gittypeignore, arena/**, build/
├── .gittypeignore                      ГЕНЕРИРУЕТСЯ bin/train, не коммитится
├── README.md                           шпаргалка команд
├── DESIGN.md                           этот файл
│
├── bin/
│   ├── train                           запуск drill-сессии по селектору
│   └── trainer                         new | diff | index | stats | due | clean
│
├── docs/
│   ├── authoring.md                    как добавить упражнение
│   ├── CATALOG.md                      ГЕНЕРИРУЕТСЯ из @-шапок
│   └── reference/                      мигрированные шпаргалки (чтение, не набор)
│
├── fixtures/src/main/java/trainer/fixtures/
│   ├── sales/        Employee, Customer, Order, Transaction, SalesData
│   ├── university/   Student, Course, Teacher, UniversityData
│   └── util/         ListNode, TreeNode, Rnd
│
├── core-drills/
│   ├── src/main/java/trainer/
│   │   ├── algorithms/  l1/ l2/ l3/ l4/ l5/
│   │   ├── collections/ l1/ l2/ l3/ l4/
│   │   ├── streams/     l1/ l2/ l3/ l4/ l5/
│   │   ├── concurrency/ l1/ l2/ l3/ l4/ l5/
│   │   ├── patterns/    l2/ l3/ l4/ l5/
│   │   └── testing/     l1/ l2/ l3/
│   ├── src/main/kotlin/trainer/
│   │   ├── kotlinlang/  l1/ l2/ l3/ l4/ l5/
│   │   └── coroutines/  l2/ l3/ l4/ l5/
│   └── src/test/…                      зеркало пакетов
│
├── spring-drills/src/{main,test}/java/trainer/{springcore,springweb,springdata}/lN/
├── integration-drills/
│   ├── src/main/java/trainer/{sql,kafka}/lN/
│   └── src/test/resources/sql/{schema.sql,seed.sql}
│
├── arena/                              gitignored: сюда bin/trainer new кладёт
│   └── src/…                           заготовку + её тест
│
├── infra/compose.yaml                  postgres + kafka для ручных экспериментов
└── progress/schedule.tsv               интервальные повторения (коммитится)
```

### 3.3 Гранулярность файла

**L1–L3: один self-contained файл, 40–120 строк, один `public final class`,
1–3 статических метода, модели — вложенные `record`.**

Почему: в режиме Wild «весь файл» становится осмысленным «набери решение
целиком», а не марафоном на 700 строк. Файлы вида `Tasks_v1.java` на 441 строку
с двадцатью задачами — режем.

**L4–L5: допустим каталог-сценарий из нескольких файлов.** Не надо прятать
controller, service, repository и Kafka listener в один гигантский класс ради
формального правила — это перестаёт быть похожим на реальный backend-код.

```
concurrency/l5/ratelimiter/
    TokenBucketRateLimiter.java     ← @task concurrency.l5.ratelimiter
    RateLimiterClock.java
    ratelimiter.md
```

Тогда `gittype core-drills/src/main/java/trainer/concurrency/l5/ratelimiter` —
сессия ровно по одному компоненту. Во всех случаях должна существовать **одна
команда**, запускающая упражнение целиком.

### 3.4 Идентификатор и выбор сессии

**ID = путь пакета без `trainer.`**: `streams.l2.AvgSalaryByDepartment`.
Из ID однозначно выводятся тема, уровень, файл, тест, sidecar. Отдельного
реестра задач нет — он бы разъезжался с кодом. `docs/CATALOG.md` генерируется
из шапок (`bin/trainer index`).

Селекторы:

```bash
bin/train streams                    # тема целиком          → путь напрямую
bin/train streams l2                 # тема + уровень        → путь напрямую
bin/train l3                         # уровень поперёк тем   → .gittypeignore
bin/train --tags groupingBy,tx       # по тегам              → .gittypeignore
bin/train --topics kafka,springdata --levels 3,4
bin/train --lang kotlin l2
bin/train due                        # что подошло по повторениям (§9)
bin/train --fresh streams            # + gittype cache clear
```

Когда селектор сводится к одному каталогу — скрипт передаёт путь напрямую и
удаляет `.gittypeignore`. Иначе генерирует ignore **явными путями, без
`!`-негаций** (классическая ловушка gitignore: нельзя re-include файл, если
исключён родительский каталог) и запускает `gittype .`.

```gitignore
# GENERATED by bin/train 2026-08-04T10:12:03 — selector: levels=3
# Do not edit, do not commit.
/arena/
/fixtures/
/docs/
/*/src/test/
/core-drills/src/main/java/trainer/streams/l1/
/core-drills/src/main/java/trainer/streams/l2/
…
```

Ignore-подход выбран вместо копирования файлов во временный репозиторий именно
потому, что сохраняет `challenges.file_path` — иначе вся аналитика по задачам
и интервальные повторения рассыпаются.

---

## 4. Уровни 1–5

| L | Название | Критерий | Время recall | Размер |
|---|---|---|---|---|
| **1** | Рефлекс | одна конструкция/API, нет edge cases, нет выбора | 5–10 мин | 10–30 LOC |
| **2** | Идиома | 2–3 операции, надо помнить сигнатуру API, явные edge cases | 10–25 мин | 20–60 LOC |
| **3** | Композиция | несколько объектов, ошибки, одна граница фреймворка. Классический middle live-coding | 25–45 мин | 50–120 LOC |
| **4** | Корректность | транзакции, конкуренция, брокер/БД, retry. Ошибка **не видна в happy path** | 45–90 мин | 100–250 LOC |
| **5** | Сценарий | несколько границ и production trade-offs при одной цели | 90–180 мин | 200–500 LOC, каталог |

Правило демаркации L3/L4: если решение пишется «по памяти шаблона» — это L3.
Если нужно доказать себе, что оно корректно (сценарий гонки, порядок коммитов,
порядок оффсетов) — это L4.

Размеры — ограничители, а не цель. L2, разросшийся до 200 строк, либо плохо
сфокусирован, либо должен быть разбит.

L5 — не «редкий алгоритм», а обычная для Middle+ задача с большим числом связей:
идемпотентная обработка события с записью в PostgreSQL и повторной доставкой Kafka.

Уровень означает сложность контекста, а не должность. Даже L1 — это то, что
Middle+ обязан писать без подсказки.

---

## 5. Карта тем и примеры задач

**algorithms** — не более 10–15 % времени.
L1 `ReverseString`, `SumArray`, `IsPalindrome` · L2 `FirstDuplicate`, `TwoSumIndices`, `BinarySearchLeftmost` ·
L3 `LongestSubstringNoRepeat`, `MergeIntervals`, `ValidBrackets`, `LinkedListCycleStart` ·
L4 `LruCacheHandRolled` (HashMap + двусвязный список), `TopKFrequent` (heap vs bucket), `TopologicalOrder` ·
L5 `externalsort`, `trie`, `consistenthash`

**collections**
L1 `MapGetOrDefault`, `ListRemoveWhileIterating` (через `Iterator.remove`) ·
L2 `ComparatorChain` (`comparing().thenComparing().reversed()` + nullsLast), `EqualsHashCodeContract` ·
L3 `TreeMapRangeQueries` (`floorKey/ceilingKey/subMap`), `ComputeIfAbsentIndex` ·
L4 `HashMapMutableKey` (задача-детектив: мутабельный ключ ломает мапу), `CustomIterable` (`Iterator` + fail-fast `modCount`)

**streams**
L1 `ActiveCustomerNames`, `CountCompleted`, `MaxSalary` (Optional) ·
L2 `AvgSalaryByDepartment` (groupingBy + TreeMap + averagingDouble), `EmailToCustomerMap` (`toMap` + merge-функция!), `TopNExpensiveOrders` ·
L3 `GroupByTypeAndStatus` (двухуровневый groupingBy), `FlatMapOrderItems`, `AboveDepartmentAverage` ·
L4 `CustomCollectorStudentStats` (свой `Collector` с корректным `combiner`), `ParallelReduceAssociativity`, `TeeingCollector` ·
L5 `pipeline` (батчинг, `Spliterator`, имитация backpressure)

**concurrency**
L1 `ThreadJoin`, `RunnableVsCallable` ·
L2 `SynchronizedCounter`, `AtomicCasIncrement`, `ExecutorSubmitAndShutdown`, `VolatileStopFlag` ·
L3 `BoundedBufferWaitNotify` (обязательно `while`, не `if`), `CompletableFutureCombine`, `ReentrantLockTryLock` ·
L4 `BoundedBufferCondition` (`Lock` + два `Condition`), `DoubleCheckedSingleton` (зачем `volatile`), `ConcurrentHashMapAtomicCompose` (`compute` vs get-then-put) ·
L5 `threadpool` (очередь, воркеры, graceful shutdown, `Future`), `ratelimiter`, `inmemorybroker`, `ttlcache`

**kotlinlang / coroutines**
L1 `DataClassAndDestructuring`, `NullSafetyChain` · L2 `ScopeFunctions`, `SealedResult`, `CollectionsVsSequences` ·
L3 `DelegatesLazyObservable`, `InlineReified`, `coroutines.RetryWithBackoff`, `coroutines.ParallelAwaitAll` ·
L4 `coroutines.StructuredConcurrencyCancellation` (`supervisorScope`, `NonCancellable`, `ensureActive`), `coroutines.FlowBufferConflate`, `VarianceInOut` ·
L5 `coroutines.workerpool` (`Channel` + `select`), `dsl` (type-safe builder)

**patterns**
L2 `BuilderWithValidation`, `StrategyEnum` · L3 `DecoratorRetry`, `FactoryMethodRegistry`, `ObserverEventBus` ·
L4 `ChainOfResponsibilityValidation`, `SingletonThreadSafeVariants` ·
L5 `atm` (выдача купюр + отказ при невозможности), `paymentlimits` (дневные/месячные лимиты + идемпотентность), `promocart`, `deliverycalculator`

**springcore** L1 `ComponentAndInjection` (почему не полевая) · L2 `ConfigurationProperties` + валидация, `ProfilesConditional` ·
L3 `QualifierAndPrimary`, `BeanLifecycleCallbacks` · L4 `AopAroundTiming`, `SelfInvocationProxyTrap`

**springweb** L1 `RestControllerCrud` · L2 `BeanValidationAndDto` · L3 `ExceptionHandlerProblemDetail` (RFC 7807), `PaginationAndSorting`, `MockMvcSliceTest` ·
L4 `IdempotencyKeyFilter`, `RestClientRetryTimeout`

**springdata** L1 `EntityAndRepository` · L2 `DerivedQueriesAndJpql`, `ProjectionsInterfaceAndDto` ·
L3 `NPlusOneFetchJoin` (тест считает запросы через Hibernate statistics — падает без фикса), `SpecificationDynamicFilter`, `OptimisticLock` (`@Version`) ·
L4 `TransactionalPropagationRequiresNew`, `LazyInitializationDetached`, `BatchInsertFlushClear` · L5 `outbox`

**sql** (§6) L1 `SelectWhereJoin`, `CountWithFilter` · L2 `GroupByHavingOrders`, `LeftJoinWithNulls`, `ManyToManyMembership` ·
L3 `SelfJoinSalaryVsManager`, `LatestRowPerId` (`DISTINCT ON` / оконка / подзапрос — три способа), `FindAndDeleteDuplicates` ·
L4 `WindowRankPerGroup`, `RunningTotalAndLag`, `AboveGroupAverageCorrelated` ·
L5 `schemadesign` (банк: клиенты/счета/транзакции — DDL + ограничения + индексы), `indexingdecision` (обосновать по `EXPLAIN`)

**kafka** (§6) L2 `ProducerConfigAndSend` (acks, retries, linger.ms), `ConsumerConfigAndPoll` ·
L3 `ManualAckListener`, `JsonSerdeAndTypeHeaders`, `KeyBasedPartitioning` (гарантия порядка по ключу) ·
L4 `IdempotentProducerAndTransactions`, `RetryTopicAndDlt`, `ConsumerRebalanceListener` (коммит оффсетов при revoke) ·
L5 `exactlyoncepipeline` (consume→transform→produce в транзакции + `read_committed`)

**testing** L1 `JUnitParameterized`, `AssertJCollections` · L2 `MockitoStubVerifyArgumentCaptor` ·
L3 `TestcontainersPostgresSlice`, `AwaitilityAsyncAssertion`

Ориентир по объёму каталога: **140–170 задач** (L1 ≈35, L2 ≈45, L3 ≈40, L4 ≈30, L5 ≈15).
Пустые клетки матрицы лучше, чем задачи ради симметрии.

---

## 6. Формат упражнения

### 6.1 Правила

Базовый факт, определяющий формат: **gittype не заставляет набирать комментарии**
(проверено). Отсюда всё остальное — пояснения ничего не стоят по времени набора,
поэтому они пишутся **по-русски и прямо на строке кода**. Это и была исходная цель
проекта: не только набивать руку, но и понимать, что происходит.

1. **Метаданные — построчными комментариями в шапке.** Парсятся `grep`, генерируют
   `docs/CATALOG.md`. `@level` и `@topic` не дублируются — они уже в пути.
2. **Условие и контракт — в Javadoc/KDoc на русском.** Смысл задачи, входные
   условия, критерий «готово».
3. **Inline-пояснения — по-русски, в конце строки или строкой выше**, с маркерами
   `WHY:` / `EDGE:` / `COST:`:
   - `WHY:` — почему выбрано именно это решение;
   - `EDGE:` — неочевидный граничный случай;
   - `COST:` — сложность или цена операции.
   Длина не ограничена жёстко: пояснение не набирается, поэтому важна только
   читаемость. Раскладку переключать не придётся — курсор проскакивает комментарий.
4. **Комментарии объясняют только неочевидное.** `// фильтруем стрим` — запрещено:
   такой комментарий занимает экран и не даёт ничего. В файле 40–120 строк обычно
   3–6 действительно полезных пояснений; больше — код тонет в тексте.
5. **Sidecar `<Task>.md` — только там, где нужна глубина**: развёрнутый разбор,
   подводные камни, follow-up с собеседования, ступенчатые подсказки. Для L1–L2
   он чаще всего не нужен — inline-пояснений хватает. Обязателен с L3.
6. **Маркеры `---8<---` вокруг тела решения** — для генератора заготовок.
7. **Никаких `main()` с закомментированными вызовами.** `main` допустим только как
   быстрый прогон примера; контракт всегда закреплён JUnit-тестом.
8. Класс `final`, методы `static`, без состояния — тест тривиален, а Wild берёт
   осмысленный кусок.

Единственное, что остаётся английским, — имена, API и устоявшиеся термины
(`downstream`, `happens-before`, `backpressure`). Переводить их вредно.

### 6.2 Пример: Java

`core-drills/src/main/java/trainer/streams/l2/AvgSalaryByDepartment.java`

```java
package trainer.streams.l2;

import trainer.fixtures.sales.Employee;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

// @task streams.l2.AvgSalaryByDepartment
// @tags groupingBy,averagingDouble,TreeMap,mapFactory
// @time 15m
// @src  leetcode75:streamExercise/Tasks_v1.java#2.2
// @doc  AvgSalaryByDepartment.md
public final class AvgSalaryByDepartment {

    private AvgSalaryByDepartment() {
    }

    /**
     * Средняя зарплата по отделам, ключи отсортированы.
     * Контракт: пустой список -> пустая Map; отдел без сотрудников не появляется.
     * Готово, когда: keySet отсортирован и не зависит от порядка входа.
     */
    public static Map<String, Double> avgSalaryByDepartment(List<Employee> employees) {
        // ---8<--- solution
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        TreeMap::new,                                // WHY: даёт отсортированные ключи; по умолчанию был бы HashMap
                        Collectors.averagingDouble(Employee::salary) // EDGE: для группы вернёт 0.0, но не null
                ));
        // --->8--- solution
    }

    /** Отделы, где все получают больше порога. */
    public static Map<String, Boolean> allAboveThreshold(List<Employee> staff, double threshold) {
        // ---8<--- solution
        return staff.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.mapping(                   // WHY: mapping адаптирует Employee -> Double до свёртки
                                Employee::salary,
                                Collectors.collectingAndThen( // COST: один проход, finisher выполняется раз на группу
                                        Collectors.toList(),
                                        list -> list.stream().allMatch(s -> s > threshold)))));
        // --->8--- solution
    }
}
```

Sidecar `AvgSalaryByDepartment.md` — по-русски:

```markdown
# streams.l2.AvgSalaryByDepartment — L2

## Идея
`groupingBy` есть в трёх формах. Двухаргументная всегда даёт `HashMap` —
порядок ключей не определён. Трёхаргументная принимает фабрику мапы:
`TreeMap::new` даёт отсортированные ключи без пересборки результата.

## Что легко сделать неправильно
- `.sorted()` до `collect` — бесполезно, `HashMap` порядок не хранит.
- `toMap` вместо `groupingBy` упадёт на дубликатах ключей без merge-функции.
- `averagingDouble` для пустой группы вернёт `0.0`, но пустая группа и не
  создаётся: ключ появляется только если был хотя бы один элемент.

## Подсказки
1. Нужен коллектор, который группирует.
2. У него есть трёхаргументная форма.
3. `groupingBy(classifier, mapFactory, downstream)`.

## Follow-up с собеседования
- Среднее, min, max и count одним проходом? → `summarizingDouble`.
- То же для параллельного стрима? → `groupingByConcurrent`, но `TreeMap`
  тогда не подойдёт как фабрика.
- Разница `mapping` и `flatMapping`?

## Связанные
streams.l3.GroupByTypeAndStatus, streams.l4.CustomCollectorStudentStats
```

### 6.3 Пример: Kotlin

`core-drills/src/main/kotlin/trainer/coroutines/l3/RetryWithBackoff.kt`

```kotlin
package trainer.coroutines.l3

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.math.min

// @task coroutines.l3.RetryWithBackoff
// @tags coroutines,retry,backoff,cancellation
// @time 20m
// @src  new
// @doc  RetryWithBackoff.md
object RetryWithBackoff {

    /**
     * Повтор [block] с экспоненциальной задержкой.
     * Контракт: отмена никогда не ретраится; последняя попытка пробрасывает свою ошибку.
     */
    suspend fun <T> retry(
        attempts: Int = 3,
        initialDelayMs: Long = 100,
        maxDelayMs: Long = 2_000,
        factor: Double = 2.0,
        block: suspend () -> T,
    ): T {
        // ---8<--- solution
        var currentDelay = initialDelayMs
        repeat(attempts - 1) {                      // EDGE: последняя попытка вынесена за цикл
            try {
                return block()
            } catch (e: CancellationException) {
                throw e                             // WHY: проглотить отмену = сломать structured concurrency
            } catch (e: Exception) {
                delay(currentDelay)                 // WHY: delay отменяемый, Thread.sleep — нет
                currentDelay = min((currentDelay * factor).toLong(), maxDelayMs)
            }
        }
        return block()
        // --->8--- solution
    }
}
```

### 6.4 Режим recall: как задача обслуживает оба режима без дублирования

Эталон — единственный источник правды. Заготовка **не хранится в git**, она
генерируется:

```bash
bin/trainer new streams.l2.AvgSalaryByDepartment
```

Скрипт копирует **и эталон, и его тест** в `arena/`, вырезая из эталона всё между
маркерами `---8<---` (вместе с inline-пояснениями — иначе это диктант, а не
задача) и подставляя `throw new UnsupportedOperationException("TODO: …")`.
Javadoc с контрактом остаётся.

`arena` — самодостаточный модуль: у него свой `src/main` и свой `src/test`,
куда попадают только те задачи, над которыми ты работаешь сейчас. Ничего не
шарится через хитрые `srcDir` из других модулей — поэтому **незаконченное
упражнение не ломает сборку остальных**, и `:arena:test` компилируется всегда.
Содержимое `arena/` в `.gitignore`, дрейф «эталон ≠ заготовка» невозможен,
потому что заготовка каждый раз генерируется заново.

```
читаю .md → bin/trainer new <id> → пишу в arena → ./gradlew :arena:test
   ├─ зелено → bin/trainer diff <id> (сравнить с эталоном) → drill: bin/train streams l2
   └─ красно → смотрю эталон, разбираю .md, задача уходит в очередь повторения
```

---

## 7. SQL и Kafka

Общее правило:

> Внешняя зависимость может блокировать **тест**, но никогда не блокирует **drill**.
> Всё, что тренируется пальцами, обязано быть обычным java/kotlin-файлом.

### SQL — гибрид

gittype не знает `sql`, значит `.sql`-файлы в drill не попадут вообще. При этом
хранить весь SQL в Java-строках неправильно: миграции, DDL и fixtures должны быть
исполняемыми `.sql`.

Решение:

- **канонический SQL — в `.sql`** (`src/test/resources/sql/schema.sql`, `seed.sql`),
  применяется контейнером через `withInitScript`;
- **ключевые запросы дополнительно — Java text block** в задаче: text block
  сохраняет форматирование и отступы, то есть типографика запроса тренируется честно;
- длинные миграции и DDL в text blocks **не** дублируются;
- **PostgreSQL, не H2**: нужны оконные функции, `DISTINCT ON`, `STRING_AGG`,
  `EXPLAIN` — H2 эмулирует их неполно, а собеседования идут по Postgres-диалекту;
- все SQL-задачи работают против **одного датасета** — экономит время и заставляет
  держать в голове одну доменную модель.

```java
// @task sql.l4.WindowRankPerGroup
// @tags window,rank,partition-by,postgres
// @src  leetcode75:docs/java-interview-tasks-catalog.md#191
public final class WindowRankPerGroup {

    /** Самые высокооплачиваемые по каждой должности; ничьи сохраняются. */
    public static final String TOP_PAID_PER_POSITION = """
            SELECT e.position, e.name, e.salary
            FROM (SELECT position, name, salary,
                         RANK() OVER (PARTITION BY position    -- WHY: RANK keeps ties, ROW_NUMBER drops them
                                      ORDER BY salary DESC) AS rk
                  FROM employees) e
            WHERE e.rk = 1                                     -- EDGE: filtering a window fn needs a subquery
            ORDER BY e.position;
            """;
}
```

### Kafka — три слоя проверки

Drill доступен всегда: Kafka-задачи — это обычные классы (`ProducerFactory` с
конфигом, `@KafkaListener`, `DefaultErrorHandler` + `DeadLetterPublishingRecoverer`,
`ConsumerRebalanceListener`, кастомный `Serializer`). Именно это и просят писать
руками. Брокер нужен только тестам:

| Уровень | Инфраструктура теста | Почему так |
|---|---|---|
| L2 | без брокера: проверяем собранную `Map<String,Object>` конфига и логику сериализатора | быстро, офлайн |
| L3 | `spring-kafka-test` `@EmbeddedKafka` | без Docker, секунды на старт; хватает для ack-режимов и партиционирования |
| L4–L5 | Testcontainers Kafka, `@Tag("integration")` | транзакции, `read_committed`, rebalance, DLT на embedded воспроизводятся ненадёжно |

In-memory broker из старого проекта остаётся **concurrency-задачей** и не
считается Kafka-треком.

---

## 8. Сборка, запуск, скрипты

```bash
./gradlew :core-drills:test                        # ежедневный цикл, секунды, без Docker
./gradlew :core-drills:test --tests '*streams*'    # одна тема
./gradlew :arena:test                              # проверка того, что написал сам
./gradlew :spring-drills:test
./gradlew :integration-drills:integrationTest      # Docker нужен только здесь
./gradlew build -PnoDocker                         # тег integration исключён, должен быть зелёным всегда
docker compose -f infra/compose.yaml up -d         # ручные эксперименты, EXPLAIN
```

Теги тестов: unit — всегда; `integration` — отдельная Gradle-задача, требует
Docker; `slow` — вне обычного цикла. Concurrency-тесты используют latch/barrier и
timeout, а не произвольный `sleep`. Состояние БД и Kafka изолируется на упражнение.

Скрипты (жёсткое ограничение: **весь тулинг ≤ ~400 строк**; как только он требует
внимания больше, чем задачи, — режем):

```bash
bin/train  [<topic>] [<level>] [--levels …] [--topics …] [--tags …] [--lang …] [--fresh] [due]
bin/trainer new <id>       эталон + тест → arena/
bin/trainer diff <id>      мой код vs эталон
bin/trainer clean          очистить arena/
bin/trainer index          перегенерировать docs/CATALOG.md
bin/trainer stats [--topic …] [--since 30d]
bin/trainer due [--limit 15]
```

Версии: JDK 21 toolchain, Kotlin 2.x, Spring Boot BOM, Testcontainers BOM,
JUnit 5, AssertJ, Mockito — всё в `gradle/libs.versions.toml`, обновляется
осознанно раз в квартал.

---

## 9. Прогресс и повторения

`bin/trainer stats` читает `~/.gittype/gittype.db` напрямую. Путь можно
переопределить через `GITTYPE_DB`, что также используется regression-тестом CLI:

```sql
SELECT c.file_path                                    AS task_file,
       COUNT(*)                                       AS runs,
       ROUND(AVG(sr.wpm), 1)                          AS avg_wpm,
       ROUND(AVG(sr.accuracy), 1)                     AS avg_acc,
       ROUND(MIN(sr.accuracy), 1)                     AS worst_acc,
       MAX(sr.completed_at)                           AS last_seen,
       CAST(julianday('now') - julianday(MAX(sr.completed_at)) AS INT) AS days_ago
FROM stage_results sr
JOIN stages       s ON s.id = sr.stage_id
JOIN challenges   c ON c.id = s.challenge_id
JOIN repositories r ON r.id = sr.repository_id
WHERE r.repository_name = 'trainer'
  AND sr.was_skipped = 0
GROUP BY c.file_path
ORDER BY days_ago DESC;
```

Дополнительные сигналы: `was_failed`/`was_skipped` — «избегаемые» задачи (трижды
пропустил — это дыра в знаниях, а не проблема набора); `consistency_streaks` (JSON)
— где рвётся ритм; `sessions.commit_hash` — привязка результата к версии эталона.

**Интервальные повторения — SM-2 lite** (полный SM-2 избыточен: у нас есть
объективный сигнал качества, субъективная оценка не нужна):

```
quality = 5, если acc ≥ 98 и wpm ≥ 0.9 × личной базы по языку
          3, если acc ≥ 95
          1, иначе (или was_failed / was_skipped)

ease     = clamp(ease + (0.1 - (5-q) × (0.08 + (5-q) × 0.02)), 1.3, 2.8)
interval = 1 день при первом успехе, 3 дня при втором, далее previous × ease
q == 1  → interval = 1, ease не растёт
```

Состояние в `progress/schedule.tsv` — коммитится, человекочитаемо, мержится глазами:

```tsv
task_id	ease	reps	interval_days	due	last_acc	last_wpm	last_seen	last_result_id
streams.l2.AvgSalaryByDepartment	2.50	4	11	2026-08-12	98.4	61.2	2026-08-01 12:00:00	81
concurrency.l4.BoundedBufferCondition	1.60	2	3	2026-08-05	93.1	44.0	2026-08-02 09:00:00	87
```

Задачи, ни разу не встречавшиеся, всегда попадают в `due` — каталог осваивается сам.

`last_result_id` делает обновление идемпотентным и позволяет последовательно
применить несколько результатов, накопившихся между запусками. CLI и формула
проверяются на изолированной SQLite fixture в `tests/tooling/progress-test.sh`.

---

## 10. Критерий готовности упражнения

Упражнение получает статус `ready`, только если:

- эталон компилируется и проходит тесты;
- **тест падает** при удалённой или очевидно неверной реализации;
- результат детерминирован, либо источник времени/случайности внедрён явно;
- проверены null / empty / duplicate / overflow, если они относятся к контракту;
- для денег `BigDecimal`, если задача не про другой выбор;
- для SQL указан диалект (по умолчанию PostgreSQL);
- интеграция проверяется настоящим PostgreSQL/Kafka через Testcontainers, а не моком;
- условие не выдаёт решение;
- комментарии объясняют только неочевидное;
- имя файла, ID, package и sidecar согласованы;
- файл отформатирован и достаточно мал для осмысленного набора;
- задача хотя бы раз прогнана в gittype и хотя бы раз решена в `arena`.

Правило против разрастания: **новая задача по теме добавляется только после того,
как предыдущая по этой теме прошла recall с зелёным тестом.**

---

## 11. Миграция из `LeetCode-75-Study-Project`

184 Java-файла, 8 Kotlin, 48 Markdown. Это банк сырья, а не готовый каталог:
решения и TODO перемешаны, старые версии дублируются, тестов мало, SQL не оформлен
как исполняемые задания, реальной Kafka нет, Spring представлен теорией. В сборке
уже виден configuration drift (README обещает Java 17, toolchain 21) — ещё одна
причина не наследовать старую структуру.

Принцип: **мигрирует содержание, не файлы.** Происхождение фиксируется в `@src`.

### Берём почти как есть

| Источник | Куда | Правки |
|---|---|---|
| `streamExercise/data_v1/*`, `data_v2/*` | `:fixtures` → `trainer.fixtures.{sales,university}` | POJO → `record`, детерминированный seed, фабричные методы вместо public static полей |
| `kotlin/arrays/TestUtils.kt` (ListNode, TreeNode) | `:fixtures` → `trainer.fixtures.util` | как есть |
| тесты ATM / PaymentLimit / Booking | `core-drills/src/test/.../patterns/l5/` | самый дорогой актив, переносятся под новые пакеты |
| `docs/*.md` (concurrency-cheatsheet, threading-qa, kotlin-*, spring-boot-jpa-guide, code-review-interview-guide) | `docs/reference/` | материал для чтения; `.md` gittype не парсит, в ignore не нужен |

### Переписываем

| Источник | Куда | Что делаем |
|---|---|---|
| **`docs/java-interview-tasks-catalog.md` — 270 задач с компанией и сложностью 1–4** | бэклог + `sql/l1..l5` (~40 SQL-задач с готовыми формулировками) | **золотая жила.** Их шкала 1–4 маппится в наши L1–L4 напрямую. Источник правды для «что писать дальше» и для приоритизации: Stream API 10 %, SQL 16 %, строки 7 %, многопоточность 6.5 %, Spring-транзакции 4.4 % |
| `docs/interview-tasks-archetypes.md` | `docs/authoring.md` | шаблоны, «подводные камни» и follow-up для секций sidecar |
| `streamExercise/Tasks_v1.java` (441 стр., ~20 задач) | `streams/l1..l3/*` | каждый метод → класс + тест + `.md`; выбросить `main` с 25 закомментированными строками |
| `Tasks_v2.java` (702 стр.), `Tasks_v2_Empty.java` | `streams/l2..l5/*` | `Tasks_v2_Empty` не нужен — его роль играет генератор заготовок |
| `StudentStatistics*.java` | `streams.l4.CustomCollectorStudentStats` | добавить тест на `combiner` (сейчас его нет) |
| `yandex/algo/AlgorithmicTasks.java` (637 стр., 13 задач) | `algorithms/l1..l3/*` | sout-«тесты» → JUnit; русские блочные комментарии → sidecar |
| `codex/ex01..ex05` | `concurrency/l2..l5/*` | ex01→L2, ex02→L3 (`wait/notify`) + L4 (`Lock`/`Condition`), ex03→`l5.threadpool`, ex04→L2/L4, ex05→`l5.inmemorybroker` |
| `concurrency/*.java` | `concurrency/l1..l4/*` | «примеры с sout» → задачи с проверяемым контрактом |
| `yandex/dev/tasks/{atm2,paymentLimit*,promoCart,deliveryCalculator}` | `patterns/l4..l5/*` | отобрать 5–6 лучших, остальное не тянуть |
| `kotlin/arrays/Solution1..5.kt` | `kotlinlang/l1..l2`, `algorithms/*` | по одной задаче на файл |
| `docs/spring-boot-jpa-guide.md` | `springdata/*`, `springweb/*` | каждый разбор → 1–2 задачи; N+1 → `springdata.l3.NPlusOneFetchJoin` с тестом на счётчик запросов |
| code-review материалы | `patterns/*` как broken-code + regression-test drills | |

### Не берём

`exercise/{ibs,ibsNew,justCode,refactor,leetcode/daily}`, `yandex/dev/tasks/old/**`,
`.idea/`, `lib/` (локальные JAR), `scripts/*.ps1`, `macOS_power_user_guide_RU.md`,
длинные курсовые документы, все `main()` с `System.out.println` вместо тестов,
файлы с именами `Main`, `Qwe`, большие сгенерированные датасеты там, где хватит
5–10 предметных объектов.

Старый репозиторий остаётся read-only архивом — не трогаем и не удаляем.

---

## 12. Roadmap

**Фаза 0 — каркас (готово 2026-08-04).**
Gradle multi-project + version catalog, `:fixtures` (перенос `data_v1`),
`.gittypeignore`, конвенции тестов, `bin/train` в простейшей форме (тема / тема+уровень)
и `bin/trainer new`. Проверить на первой же сессии R2 (сканер обработал ровно
выбранные исходные файлы) и R3 (как режется Java text block). Число пунктов
`challenges available` с числом файлов не сравнивается: один файл закономерно
даёт несколько кусков для каждой сложности gittype.

**Фаза 1 — вертикальный срез (готово 2026-08-04).**
По **одному полностью готовому** упражнению на трек: collections L1, streams L2,
kotlin L2, SQL L2, Spring MVC/JPA L3, Kafka consumer L3. Цель фазы — проверить
формат, команды и длительность, а не количество. Именно здесь дёшево менять
решения; после 150 задач это будет дорого.

Реализованный срез: `collections.l1.MapGetOrDefault`,
`streams.l2.AvgSalaryByDepartment`, `kotlinlang.l2.SealedResult`,
`sql.l2.GroupByHavingOrders`, `springweb.l3.ExceptionHandlerProblemDetail`,
`kafka.l3.ManualAckListener`. Быстрые тесты, MockMvc и Embedded Kafka зелёные;
PostgreSQL Testcontainers-тест зелёный на Docker Desktop с образом
`postgres:17.6-alpine`.

**Фаза 2 — первая полезная библиотека (готово 2026-08-04), 30 новых задач.**
5 collections/algorithms, 5 streams, 5 kotlin, 3 concurrency/coroutines,
4 SQL, 4 Spring, 2 Kafka, 2 бизнес-сценария. Плюс генератор `.gittypeignore`
(селекторы по уровню и тегам) и `bin/trainer index`.

Итого в каталоге 36 упражнений. SQL-задачи имеют быстрый contract-тест и
семантический PostgreSQL Testcontainers-тест; обе новые Kafka-задачи проверены
на Embedded Kafka. Составные селекторы доступны по темам, уровням, тегам и языку,
а `TRAINER_DRY_RUN=1` позволяет проверить плейлист без запуска TUI.

**Фаза 3 — workflow (готово 2026-08-04).**
`bin/trainer stats` на sqlite, `progress/schedule.tsv`, `bin/train due`.
Повторение слабых тем вместо случайной выдачи.

CLI сведён в `tools/trainer.py`; два публичных shell-wrapper и helper занимают
менее 400 строк, сохраняя ограничение §8. Обновление schedule атомарно, повторный
запуск идемпотентен, а несколько новых результатов применяются по порядку.

**Фаза 4 — верх пирамиды (по мере надобности).**
L4/L5: transaction isolation, idempotency, N+1, retry/DLT, outbox, exactly-once;
concurrency L5 (threadpool, ratelimiter, broker); SQL L5; code-review drills; CI.

Правило приоритета: **контент важнее тулинга.** Если выбор между «ещё 10 задач» и
«улучшить скрипт» — сначала задачи. Тулинг Фаз 0–2 покрывает 90 % ежедневного
использования.

---

## 13. Риски и открытые вопросы

**R1 — закрыт (2026-08-04).** Вопрос был: набираются ли комментарии в gittype.
**Ответ: нет, gittype их пропускает.** Проверено в живой сессии.
Следствия, уже учтённые в §6: пояснения пишутся по-русски прямо на строке кода;
ограничение «ASCII-английский ≤70 символов» снято; sidecar `.md` из обязательного
превратился в необязательный для L1–L2 — это заметно снижает стоимость написания
одной задачи, что важно при каталоге в 150 задач. Запасной план со `strip`-зеркалом
не нужен и выброшен.

**R2. `.gittypeignore` может не поддержать всё, что мы генерируем.**
Митигация: только явные пути, без `!`-негаций и `**`-хитростей. Проверка при первой
генерации: сравнить счётчик обработанных файлов сканера с числом файлов в селекторе.
Базовая проверка прямого пути в Фазе 0 пройдена: `streams/l2` показал `1/1 files`.
Из этого файла gittype 0.10.1 создал 8 Easy-challenges — это ожидаемые варианты
кусков, а не восемь задач. Проверка сгенерированного ignore остаётся на Фазу 2.

**R3. tree-sitter и наши конструкции.** Многострочные Java text blocks (SQL),
Kotlin DSL, многострочные Spring-аннотации могут резаться в неудобные куски.
Проба Фазы 0 пройдена частично: Java text block разобран без ошибки, исходный файл
обработан как `1/1 files`, куски созданы для всех сложностей вплоть до Wild.
Эргономику границ куска надо проверить руками на первой настоящей SQL-задаче.
Запасной вариант — конкатенация строк вместо text block (хуже читается, парсится
предсказуемо).

**R4. Заучивание вместо понимания.** Через 10 повторов пальцы помнят символы, а
голова нет. Митигация: (а) recall обязателен — задача не освоена без зелёного
`:arena:test`; (б) `due` понижает частоту выученных задач; (в) раз в месяц читать
sidecar без кода.

**R5. Инфраструктурный дрейф.** Testcontainers, Spring BOM, embedded Kafka ломаются
на обновлениях и съедают вечера. Митигация: версии в `libs.versions.toml`,
обновление раз в квартал, Docker-тесты всегда за тегом, `build -PnoDocker` зелёный всегда.

**R6. Кэш gittype после правки эталонов** — может отдавать старый текст.
Правило: после правки первая сессия с `--fresh`.

**R7. Объём каталога vs время.** 150+ задач с тестами и sidecar — десятки часов.
Митигация: фазы упорядочены по отдаче, после Фазы 2 репозиторий уже полезен ежедневно.

**R8. `package`/`import` набираются каждый раз.** Короткие пакеты, `java.util.*`
вместо десяти строк импортов. Если импорты будут раздражать в Wild — сместить
фокус на Hard.

### Открытые вопросы (не блокируют старт)

1. Java-first или Kotlin-first для Spring-примеров. Дублировать каждый пример на
   двух языках не следует. Предварительно — Java-first, Kotlin живёт в `kotlinlang`/`coroutines`.
2. Нужна ли третья шкала «частота на собеседованиях» (`@freq high|mid|low`) для
   приоритизации `due`. Данные для неё уже есть в каталоге 270 задач. Скорее да, Фаза 3.
3. Коммитить ли `arena/` в отдельной ветке ради истории своих решений. Сейчас — нет.
4. Нужен ли чистый `.sql` typing вне gittype, или хватит text blocks для избранных запросов.
