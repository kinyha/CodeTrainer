package trainer.kotlinlang.l1

// @task kotlinlang.l1.ExtensionFunctionOnString
// @tags extension-function,String
// @time 6m
// @src  new

/** Extension-функция — вызывается как метод на String, но не требует менять сам класс String. */
fun String.truncate(maxLength: Int): String {
    // ---8<--- solution
    return if (length <= maxLength) this else take(maxLength) + "…"
    // --->8--- solution
}
