package trainer.kotlinlang.l1

// @task kotlinlang.l1.ImmutableProfileUpdate
// @tags data-class,copy,immutability,nested
// @time 8m
// @src  new
object ImmutableProfileUpdate {

    data class Notifications(val email: Boolean, val push: Boolean)
    data class Profile(val id: Long, val name: String, val notifications: Notifications)

    /** Включает email-уведомления, не меняя исходный профиль и остальные поля. */
    fun enableEmail(profile: Profile): Profile {
        // ---8<--- solution
        return profile.copy(
            notifications = profile.notifications.copy(email = true),
        )
        // --->8--- solution
    }
}
