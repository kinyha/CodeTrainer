package trainer.kotlinlang.l3

// @task kotlinlang.l3.RunCatchingResult
// @tags runCatching,Result,exception-to-value
// @time 20m
// @src  new
object RunCatchingResult {

    /** runCatching превращает исключение в значение Result — try/catch на месте вызова не нужен. */
    fun parsePositiveInt(raw: String): Result<Int> {
        // ---8<--- solution
        return runCatching { raw.trim().toInt() }
            .mapCatching { value -> if (value > 0) value else error("must be positive: $value") }
        // --->8--- solution
    }
}
