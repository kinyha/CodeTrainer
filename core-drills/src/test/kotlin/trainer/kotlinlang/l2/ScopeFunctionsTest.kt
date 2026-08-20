package trainer.kotlinlang.l2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScopeFunctionsTest {

    @Test
    fun `builds a summary string with the total`() {
        assertThat(ScopeFunctions.buildSummary("Sales", listOf(10, 20, 30))).isEqualTo("Sales: 60")
    }

    @Test
    fun `empty values give a zero total`() {
        assertThat(ScopeFunctions.buildSummary("Empty", emptyList())).isEqualTo("Empty: 0")
    }
}
