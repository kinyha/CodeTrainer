package trainer.kotlinlang.l2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class CollectionsVsSequencesTest {

    @Test
    fun `finds the first square above the threshold`() {
        val invocations = AtomicInteger()
        assertThat(CollectionsVsSequences.firstSquareAbove(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 20, invocations))
            .isEqualTo(25)
    }

    @Test
    fun `stops mapping as soon as a match is found`() {
        val invocations = AtomicInteger()
        CollectionsVsSequences.firstSquareAbove(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 20, invocations)

        assertThat(invocations.get()).isEqualTo(5) // квадраты 1,4,9,16,25 - остановка на 25
    }
}
