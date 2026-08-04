package trainer.kotlinlang.l2

// @task kotlinlang.l2.LazySequenceWindow
// @tags sequence,lazy,map,filter,take
// @time 12m
// @src  new
object LazySequenceWindow {

    /** Лениво находит первые limit квадратов чётных чисел. */
    fun firstEvenSquares(values: Sequence<Int>, limit: Int): List<Int> {
        require(limit >= 0) { "limit must be non-negative" }

        // ---8<--- solution
        return values
            .filter { it % 2 == 0 }
            .map { it * it }
            .take(limit)
            .toList()
        // --->8--- solution
    }
}
