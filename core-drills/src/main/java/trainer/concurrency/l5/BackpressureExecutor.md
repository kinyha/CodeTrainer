# BackpressureExecutor — L5

## Идея

`Semaphore` ограничивает число принятых, но ещё не завершённых задач. Permit
освобождается и после выполнения, и если delegate отверг submit.

## Что легко сделать неправильно

- освободить permit сразу после `execute`, а не после `run`;
- утечь permit при `RejectedExecutionException`;
- проглотить interrupt ожидающего submitter.

## Follow-up

Когда блокирующий backpressure хуже bounded queue с явным rejection policy?
