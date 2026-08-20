package trainer.kotlinlang.l3

import kotlin.properties.Delegates

// @task kotlinlang.l3.DelegatesLazyObservable
// @tags delegated-properties,lazy,observable
// @time 20m
// @src  new
object DelegatesLazyObservable {

    class Settings(private val systemLocale: String) {
        var onThemeChanged: ((old: String, new: String) -> Unit)? = null

        /** by lazy {} вычисляется один раз, при первом обращении, и дальше отдаётся из кэша. */
        val defaultTheme: String by lazy {
            // ---8<--- solution
            if (systemLocale.startsWith("ar") || systemLocale.startsWith("he")) "dark-rtl" else "light"
            // --->8--- solution
        }

        /** Delegates.observable вызывает колбэк ПОСЛЕ каждого присваивания нового значения. */
        var theme: String by Delegates.observable("light") { _, old, new ->
            onThemeChanged?.invoke(old, new)
        }
    }
}
