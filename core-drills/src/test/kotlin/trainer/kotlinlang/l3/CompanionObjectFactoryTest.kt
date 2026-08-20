package trainer.kotlinlang.l3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompanionObjectFactoryTest {

    @Test
    fun `parses a valid trimmed email`() {
        val email = CompanionObjectFactory.Email.parse("  user@example.com  ")
        assertThat(email?.value).isEqualTo("user@example.com")
    }

    @Test
    fun `rejects an email missing the at sign`() {
        assertThat(CompanionObjectFactory.Email.parse("not-an-email")).isNull()
    }

    @Test
    fun `rejects an email with the at sign at the edges`() {
        assertThat(CompanionObjectFactory.Email.parse("@example.com")).isNull()
        assertThat(CompanionObjectFactory.Email.parse("user@")).isNull()
    }
}
