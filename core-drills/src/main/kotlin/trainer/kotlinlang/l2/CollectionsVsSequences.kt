package trainer.kotlinlang.l2

import java.util.concurrent.atomic.AtomicInteger

// @task kotlinlang.l2.CollectionsVsSequences
// @tags sequence,eager-vs-lazy,asSequence
// @time 12m
// @src  new
object CollectionsVsSequences {

    /**
     * List.map() строит промежуточный список целиком, прежде чем first() что-то возьмёт.
     * asSequence() выполняет map лениво, элемент за элементом — first() останавливает
     * вычисление сразу после первого подходящего элемента.
     */
    fun firstSquareAbove(values: List<Int>, threshold: Int, invocations: AtomicInteger): Int {
        // ---8<--- solution
        return values.asSequence()
            .map { invocations.incrementAndGet(); it * it }
            .first { it > threshold }
        // --->8--- solution
    }
}
