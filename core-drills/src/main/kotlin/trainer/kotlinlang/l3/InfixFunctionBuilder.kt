package trainer.kotlinlang.l3

// @task kotlinlang.l3.InfixFunctionBuilder
// @tags infix-function,readability
// @time 18m
// @src  new
object InfixFunctionBuilder {

    data class Range(val from: Int, val to: Int) {
        operator fun contains(value: Int): Boolean = value in from..to
    }

    /** infix убирает точку и скобки: 1 upTo 10 читается почти как естественный язык. */
    infix fun Int.upTo(end: Int): Range {
        // ---8<--- solution
        require(end >= this) { "end must not be before start" }
        return Range(this, end)
        // --->8--- solution
    }
}
