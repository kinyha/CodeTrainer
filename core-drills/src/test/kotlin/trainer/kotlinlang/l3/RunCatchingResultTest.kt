package trainer.kotlinlang.l3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RunCatchingResultTest {

    @Test
    fun `parses a valid positive integer`() {
        val result = RunCatchingResult.parsePositiveInt(" 42 ")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(42)
    }

    @Test
    fun `fails on non numeric input`() {
        assertThat(RunCatchingResult.parsePositiveInt("abc").isFailure).isTrue()
    }

    @Test
    fun `fails on non positive input`() {
        assertThat(RunCatchingResult.parsePositiveInt("0").isFailure).isTrue()
        assertThat(RunCatchingResult.parsePositiveInt("-5").isFailure).isTrue()
    }
}
