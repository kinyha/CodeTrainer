package trainer.kotlinlang.l2

// @task kotlinlang.l2.SealedResult
// @tags sealed-interface,when,generics,result
// @time 15m
// @src  new
// @doc  SealedResult.md
object SealedResult {

    sealed interface Outcome<out T> {
        data class Success<T>(val value: T) : Outcome<T>
        data class Failure(val message: String, val cause: Throwable? = null) : Outcome<Nothing>
    }

    /**
     * Преобразует успешное значение и не трогает ошибку.
     * Готово, когда when исчерпывающий и Failure сохраняется без пересоздания.
     */
    fun <T, R> map(outcome: Outcome<T>, transform: (T) -> R): Outcome<R> {
        // ---8<--- solution
        return when (outcome) {
            is Outcome.Success -> Outcome.Success(transform(outcome.value))
            is Outcome.Failure -> outcome // WHY: Nothing ковариантен, поэтому ошибка подходит для Outcome<R>
        }
        // --->8--- solution
    }

    /** Возвращает запасное значение только для ошибки. */
    fun <T> recover(outcome: Outcome<T>, fallback: (Outcome.Failure) -> T): Outcome<T> {
        // ---8<--- solution
        return when (outcome) {
            is Outcome.Success -> outcome
            is Outcome.Failure -> Outcome.Success(fallback(outcome))
        }
        // --->8--- solution
    }
}
