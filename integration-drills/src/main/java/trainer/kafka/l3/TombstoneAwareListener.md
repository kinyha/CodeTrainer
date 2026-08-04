# TombstoneAwareListener — L3

## Идея

В compacted topic запись с `null` payload — tombstone: ключ нужно удалить из
локального состояния. `null` key при этом остаётся нарушением контракта.

## Что легко сделать неправильно

- отфильтровать `null` payload как невалидное сообщение;
- удалить по payload вместо Kafka key;
- считать compaction мгновенным удалением старых записей.

## Follow-up

Как восстановить materialized state после полной перезагрузки consumer?
