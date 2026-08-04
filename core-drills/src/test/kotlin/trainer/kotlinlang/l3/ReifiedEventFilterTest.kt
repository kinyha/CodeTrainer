package trainer.kotlinlang.l3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReifiedEventFilterTest {

    @Test
    fun `filters subtype and preserves encounter order`() {
        val events: List<ReifiedEventFilter.Event> = listOf(
            ReifiedEventFilter.Created(1),
            ReifiedEventFilter.Renamed(1, "first"),
            ReifiedEventFilter.Deleted(2),
            ReifiedEventFilter.Renamed(2, "second"),
        )

        assertThat(ReifiedEventFilter.only<ReifiedEventFilter.Renamed>(events))
            .containsExactly(
                ReifiedEventFilter.Renamed(1, "first"),
                ReifiedEventFilter.Renamed(2, "second"),
            )
    }
}
