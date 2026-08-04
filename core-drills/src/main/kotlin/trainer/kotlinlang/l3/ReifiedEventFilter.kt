package trainer.kotlinlang.l3

// @task kotlinlang.l3.ReifiedEventFilter
// @tags inline,reified,filterIsInstance,generics
// @time 18m
// @src  new
object ReifiedEventFilter {

    sealed interface Event
    data class Created(val id: Long) : Event
    data class Renamed(val id: Long, val name: String) : Event
    data class Deleted(val id: Long) : Event

    /** Фильтрует события по T без передачи Class/KClass. */
    inline fun <reified T : Event> only(events: Iterable<Event>): List<T> {
        // ---8<--- solution
        return events.filterIsInstance<T>()
        // --->8--- solution
    }
}
