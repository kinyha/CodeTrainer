package trainer.kotlinlang.l4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TailrecSumTest {

    @Test
    fun `sums a small list`() {
        assertThat(TailrecSum.sum(listOf(1, 2, 3, 4, 5))).isEqualTo(15)
    }

    @Test
    fun `empty list sums to zero`() {
        assertThat(TailrecSum.sum(emptyList())).isEqualTo(0)
    }

    @Test
    fun `does not overflow the stack on a million elements`() {
        val values = List(1_000_000) { 1 }
        assertThat(TailrecSum.sum(values)).isEqualTo(1_000_000L)
    }
}
