package trainer.kotlinlang.l4

// @task kotlinlang.l4.InlineValueClassWrapper
// @tags value-class,inline-class,type-safety
// @time 25m
// @src  new
object InlineValueClassWrapper {

    /**
     * value class оборачивает Long почти без накладных расходов на боксинг (стирается на этапе
     * компиляции в большинстве случаев), но при этом типобезопасен: UserId и OrderId нельзя
     * перепутать местами, хотя оба — обёртки над Long.
     */
    @JvmInline
    value class UserId(val raw: Long) {
        init {
            require(raw > 0) { "raw must be positive" }
        }
    }

    @JvmInline
    value class OrderId(val raw: Long)

    fun describe(userId: UserId, orderId: OrderId): String {
        // ---8<--- solution
        return "user ${userId.raw} placed order ${orderId.raw}"
        // --->8--- solution
    }
}
