package trainer.kotlinlang.l4

// @task kotlinlang.l4.TailrecSum
// @tags tailrec,recursion,stack-safety
// @time 22m
// @src  new
object TailrecSum {

    /**
     * tailrec требует, чтобы рекурсивный вызов был ПОСЛЕДНИМ действием — тогда компилятор
     * развернёт его в цикл, и стек не переполнится даже на миллионах вызовов.
     */
    tailrec fun sum(values: List<Int>, index: Int = 0, acc: Long = 0): Long {
        // ---8<--- solution
        if (index == values.size) return acc
        return sum(values, index + 1, acc + values[index])
        // --->8--- solution
    }
}
