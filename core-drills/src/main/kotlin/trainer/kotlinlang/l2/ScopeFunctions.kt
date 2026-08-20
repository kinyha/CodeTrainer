package trainer.kotlinlang.l2

// @task kotlinlang.l2.ScopeFunctions
// @tags scope-functions,apply,also,let
// @time 12m
// @src  new
object ScopeFunctions {

    data class Report(var title: String = "", var total: Int = 0)

    /**
     * apply настраивает получателя и возвращает ЕГО (для конфигурации); also выполняет
     * побочный эффект и тоже возвращает получателя; let трансформирует и возвращает
     * результат лямбды, а не получателя.
     */
    fun buildSummary(title: String, values: List<Int>): String {
        // ---8<--- solution
        return Report()
            .apply {
                this.title = title
                this.total = values.sum()
            }
            .also { report -> check(report.total >= 0) { "total must not be negative" } }
            .let { report -> "${report.title}: ${report.total}" }
        // --->8--- solution
    }
}
