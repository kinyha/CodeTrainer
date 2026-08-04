# ValidatedCreateCustomer — L3

## Идея

`@Valid` запускает Bean Validation до тела controller. Нормализация выполняется
только для уже валидного DTO, malformed request возвращает 400.

## Что легко сделать неправильно

- забыть `@Valid` у `@RequestBody`;
- ожидать, что `@Email` сам trim-ит пробелы;
- вернуть entity вместо отдельного response DTO.

## Follow-up

Как сделать единый RFC 9457 response со списком field errors?
