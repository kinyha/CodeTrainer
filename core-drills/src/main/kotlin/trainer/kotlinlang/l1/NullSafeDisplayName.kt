package trainer.kotlinlang.l1

// @task kotlinlang.l1.NullSafeDisplayName
// @tags null-safety,safe-call,elvis,takeIf
// @time 7m
// @src  new
object NullSafeDisplayName {

    data class User(val name: String?)

    /** Возвращает trimmed-имя либо Anonymous для null и пустого имени. */
    fun resolve(user: User?): String {
        // ---8<--- solution
        return user?.name
            ?.trim()
            ?.takeIf(String::isNotEmpty)
            ?: "Anonymous"
        // --->8--- solution
    }
}
