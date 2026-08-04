# PlaceOrderWithOutbox — L5

## Идея

Aggregate и outbox row записываются одной локальной транзакцией. Отдельный
publisher позже отправляет event и помечает row опубликованной.

## Что легко сделать неправильно

- сначала commit order, затем напрямую отправить Kafka;
- генерировать недетерминированный payload без версии схемы;
- удалять outbox до подтверждения broker.

## Follow-up

Как Debezium CDC меняет publisher, retry и порядок событий одного aggregate?
