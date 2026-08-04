package trainer.kotlinlang.l2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import trainer.kotlinlang.l2.SealedResult.Outcome

class SealedResultTest {

    @Test
    fun `map transforms success`() {
        val result = SealedResult.map(Outcome.Success(21)) { it * 2 }

        assertThat(result).isEqualTo(Outcome.Success(42))
    }

    @Test
    fun `map preserves failure and does not call transform`() {
        val failure = Outcome.Failure("timeout", IllegalStateException("network"))
        var calls = 0

        val result = SealedResult.map(failure) {
            calls++
            it
        }

        assertThat(result).isSameAs(failure)
        assertThat(calls).isZero()
    }

    @Test
    fun `recover changes only failure`() {
        val recovered = SealedResult.recover(Outcome.Failure("missing")) { 0 }
        val success = Outcome.Success(7)

        assertThat(recovered).isEqualTo(Outcome.Success(0))
        assertThat(SealedResult.recover(success) { 0 }).isSameAs(success)
    }
}
