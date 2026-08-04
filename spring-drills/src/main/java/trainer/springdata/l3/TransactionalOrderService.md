# TransactionalOrderService — L3

## Идея

Проверка существования и изменение статуса находятся в одной public
transactional boundary. Аннотация на private self-invoked метод не дала бы proxy.

## Что легко сделать неправильно

- поставить `@Transactional` на внутренний метод того же bean;
- сделать check и write в разных транзакциях;
- поймать исключение и закоммитить частичное состояние.

## Follow-up

Как защитить статус от конкурентного cancel через optimistic locking?
