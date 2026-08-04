# IdempotentPaymentService — L4

## Идея

`ConcurrentHashMap.compute` сериализует обработку одного payment id. Успешный
receipt кэшируется, исключение gateway не создаёт запись и допускает повтор.

## Что легко сделать неправильно

- сделать `get` + `put` и дважды вызвать gateway в гонке;
- принять тот же id с другим payload;
- навсегда закэшировать временную ошибку.

## Follow-up

Как перенести idempotency key в PostgreSQL и пережить рестарт процесса?
