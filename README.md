# Trainer

Персональный тренажёр Java/Kotlin backend-навыков. Полный замысел, формат задач
и roadmap описаны в [DESIGN.md](DESIGN.md).

## Быстрый старт

```bash
./gradlew :core-drills:test
bin/train streams l2
bin/trainer new streams.l2.AvgSalaryByDepartment
./gradlew :arena:test
```

`bin/train` запускает эталон в gittype. `bin/trainer new` создаёт в `arena/`
заготовку для самостоятельного решения и копирует её тест. Незаконченная задача
в арене не влияет на остальные модули.

## Модули

- `fixtures` — общие неизменяемые модели и детерминированные данные;
- `core-drills` — Java/Kotlin без Spring и Docker;
- `spring-drills` — Spring MVC, Core и Data;
- `integration-drills` — PostgreSQL и Kafka, тяжёлые тесты вынесены отдельно;
- `arena` — локальная рабочая область recall-режима, её `src/` не коммитится.

## Проверки

```bash
./gradlew :core-drills:test
./gradlew :spring-drills:test
./gradlew :integration-drills:test
./gradlew :integration-drills:integrationTest  # нужен Docker
./gradlew build -PnoDocker
```

## Готовый вертикальный срез

- `collections.l1.MapGetOrDefault` — частотная Map и порядок первого появления;
- `streams.l2.AvgSalaryByDepartment` — downstream collector и фабрика `TreeMap`;
- `kotlinlang.l2.SealedResult` — sealed-иерархия и исчерпывающий `when`;
- `sql.l2.GroupByHavingOrders` — PostgreSQL `GROUP BY/HAVING` в Java text block;
- `springweb.l3.ExceptionHandlerProblemDetail` — RFC 9457 через Spring MVC;
- `kafka.l3.ManualAckListener` — manual offset acknowledgment с Embedded Kafka-тестом.

SQL-контракт проверяется быстрым unit-тестом всегда, а семантика запроса — настоящим
PostgreSQL через `:integration-drills:integrationTest` при запущенном Docker.
