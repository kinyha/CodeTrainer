package trainer.kotlinlang.l1

// @task kotlinlang.l1.NullSafetyChain
// @tags null-safety,safe-call,elvis,nested
// @time 7m
// @src  new
object NullSafetyChain {

    data class Address(val city: String?)
    data class Customer(val address: Address?)
    data class Order(val customer: Customer?)

    /** Каждое звено цепочки может быть null — safe-call коротко замыкает на первом null. */
    fun cityOrUnknown(order: Order?): String {
        // ---8<--- solution
        return order?.customer?.address?.city ?: "Unknown"
        // --->8--- solution
    }
}
