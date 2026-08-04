package trainer.kotlinlang.l2

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class LazySequenceWindowTest {

    @Test
    fun `takes requested window without consuming infinite source`() {
        val naturals = generateSequence(1) { it + 1 }
        assertThat(LazySequenceWindow.firstEvenSquares(naturals, 3)).containsExactly(4, 16, 36)
    }

    @Test
    fun `rejects negative limit`() {
        assertThatIllegalArgumentException()
            .isThrownBy { LazySequenceWindow.firstEvenSquares(emptySequence(), -1) }
    }
}
