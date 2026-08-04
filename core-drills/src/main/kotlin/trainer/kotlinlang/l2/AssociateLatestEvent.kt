package trainer.kotlinlang.l2

// @task kotlinlang.l2.AssociateLatestEvent
// @tags associate,fold,data-class,duplicates
// @time 15m
// @src  new
object AssociateLatestEvent {

    data class Event(val aggregateId: Long, val revision: Int, val payload: String)

    /** Для каждого aggregateId оставляет событие с наибольшей revision. */
    fun index(events: List<Event>): Map<Long, Event> {
        // ---8<--- solution
        return events.fold(linkedMapOf()) { result, event ->
            val current = result[event.aggregateId]
            if (current == null || event.revision > current.revision) {
                result[event.aggregateId] = event
            }
            result
        }
        // --->8--- solution
    }
}
