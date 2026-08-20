package trainer.kotlinlang.l1

// @task kotlinlang.l1.WhenExpressionExhaustive
// @tags when,sealed-interface,exhaustive
// @time 7m
// @src  new
object WhenExpressionExhaustive {

    sealed interface TrafficLight {
        object Red : TrafficLight
        object Yellow : TrafficLight
        object Green : TrafficLight
    }

    /** when как ВЫРАЖЕНИЕ по sealed-иерархии: без else компилятор проверит полноту веток. */
    fun action(light: TrafficLight): String {
        // ---8<--- solution
        return when (light) {
            TrafficLight.Red -> "Stop"
            TrafficLight.Yellow -> "Caution"
            TrafficLight.Green -> "Go"
        }
        // --->8--- solution
    }
}
