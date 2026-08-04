# ReifiedEventFilter — L3

## Идея

`inline reified` сохраняет тип `T` в месте вызова, поэтому
`filterIsInstance<T>()` не требует передавать `Class` или `KClass`.

## Что легко сделать неправильно

- убрать `inline` и потерять возможность `reified`;
- сделать небезопасный cast всего списка;
- потерять encounter order при промежуточной группировке.

## Follow-up

Когда явный `KClass<T>` лучше inline-функции, несмотря на более шумный API?
