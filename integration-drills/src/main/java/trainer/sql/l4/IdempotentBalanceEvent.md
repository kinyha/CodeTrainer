# IdempotentBalanceEvent — L4

## Идея

Unique event id и изменение balance объединены одним SQL statement. CTE отдаёт
строку только для нового event, поэтому duplicate не выполняет update.

## Что легко сделать неправильно

- сделать отдельные check и update с окном гонки;
- записать event для несуществующего account;
- считать Kafka exactly-once заменой constraint в PostgreSQL.

## Follow-up

Как сохранить результат обработки, чтобы duplicate получил прежний response?
