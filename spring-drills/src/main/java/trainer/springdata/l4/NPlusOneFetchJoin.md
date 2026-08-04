# NPlusOneFetchJoin — L4

## Идея

`LEFT JOIN FETCH` инициализирует collection в исходном select, `DISTINCT`
устраняет повтор root entity. Тест считает реальные prepared statements Hibernate.

## Что легко сделать неправильно

- использовать обычный `JOIN`, не fetch;
- лечить N+1 сменой LAZY на EAGER;
- одновременно fetch-join две bag collections и получить декартово произведение.

## Follow-up

Когда entity graph или batch fetching лучше явного fetch join?
