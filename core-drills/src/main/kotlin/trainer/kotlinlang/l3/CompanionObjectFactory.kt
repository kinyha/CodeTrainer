package trainer.kotlinlang.l3

// @task kotlinlang.l3.CompanionObjectFactory
// @tags companion-object,factory,private-constructor
// @time 18m
// @src  new
object CompanionObjectFactory {

    class Email private constructor(val value: String) {
        companion object {
            /** companion object — идиоматичная замена static factory method из Java. */
            fun parse(raw: String): Email? {
                // ---8<--- solution
                val trimmed = raw.trim()
                return if (trimmed.contains("@") && !trimmed.startsWith("@") && !trimmed.endsWith("@")) {
                    Email(trimmed)
                } else {
                    null
                }
                // --->8--- solution
            }
        }
    }
}
