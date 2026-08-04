package trainer.kotlinlang.l1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ImmutableProfileUpdateTest {

    @Test
    fun `copies nested value without losing push setting`() {
        val source = ImmutableProfileUpdate.Profile(
            id = 7,
            name = "Ada",
            notifications = ImmutableProfileUpdate.Notifications(email = false, push = true),
        )

        val updated = ImmutableProfileUpdate.enableEmail(source)

        assertThat(updated.notifications.email).isTrue()
        assertThat(updated.notifications.push).isTrue()
        assertThat(source.notifications.email).isFalse()
        assertThat(updated.id).isEqualTo(source.id)
    }
}
