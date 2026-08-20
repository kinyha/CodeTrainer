package trainer.kotlinlang.l2

// @task kotlinlang.l2.OperatorOverloading
// @tags operator-overloading,plus,value-type
// @time 12m
// @src  new
object OperatorOverloading {

    data class Money(val cents: Long) {
        /** operator fun позволяет писать money1 + money2 вместо money1.add(money2). */
        operator fun plus(other: Money): Money {
            // ---8<--- solution
            return Money(cents + other.cents)
            // --->8--- solution
        }

        operator fun times(factor: Int): Money {
            return Money(cents * factor)
        }
    }
}
