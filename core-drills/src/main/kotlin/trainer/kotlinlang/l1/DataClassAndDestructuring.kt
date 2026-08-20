package trainer.kotlinlang.l1

// @task kotlinlang.l1.DataClassAndDestructuring
// @tags data-class,destructuring,component
// @time 6m
// @src  new
object DataClassAndDestructuring {

    data class Point(val x: Int, val y: Int)

    /** data class даёт componentN() бесплатно — swap через destructuring без временной переменной. */
    fun swapped(point: Point): Point {
        // ---8<--- solution
        val (x, y) = point
        return Point(y, x)
        // --->8--- solution
    }
}
