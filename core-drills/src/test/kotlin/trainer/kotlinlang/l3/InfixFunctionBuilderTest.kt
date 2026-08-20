package trainer.kotlinlang.l3

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import trainer.kotlinlang.l3.InfixFunctionBuilder.upTo

class InfixFunctionBuilderTest {

    @Test
    fun `builds an inclusive range using infix syntax`() {
        val range = 1 upTo 10
        assertThat(range.from).isEqualTo(1)
        assertThat(range.to).isEqualTo(10)
        assertThat(5 in range).isTrue()
        assertThat(11 in range).isFalse()
    }

    @Test
    fun `single point range is allowed`() {
        val range = 5 upTo 5
        assertThat(5 in range).isTrue()
    }

    @Test
    fun `rejects end before start`() {
        assertThatThrownBy { 10 upTo 1 }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
