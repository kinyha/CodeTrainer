# RetryToDltListener — L4

## Идея

`@RetryableTopic` создаёт неблокирующие retry topics; после трёх неудачных
попыток запись приходит в `@DltHandler`. DLT — отдельный бизнес-процесс, не лог.

## Что легко сделать неправильно

- ретраить non-retryable validation error;
- не сделать handler идемпотентным;
- включить бесконечный retry DLT и получить цикл.

## Follow-up

Почему non-blocking retry нельзя совмещать с batch listener и container transaction?

Документация: [Spring Kafka — retry topic configuration](https://docs.spring.io/spring-kafka/reference/retrytopic/retry-config.html).
