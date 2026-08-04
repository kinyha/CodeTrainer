package trainer.kotlinlang.l1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NullSafeDisplayNameTest {

    @Test
    fun `trims present name`() {
        assertThat(NullSafeDisplayName.resolve(NullSafeDisplayName.User("  Ada  "))).isEqualTo("Ada")
    }

    @Test
    fun `uses fallback for every absent form`() {
        assertThat(NullSafeDisplayName.resolve(null)).isEqualTo("Anonymous")
        assertThat(NullSafeDisplayName.resolve(NullSafeDisplayName.User(null))).isEqualTo("Anonymous")
        assertThat(NullSafeDisplayName.resolve(NullSafeDisplayName.User("  "))).isEqualTo("Anonymous")
    }
}
