package trainer.kotlinlang.l2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AssociateLatestEventTest {

    @Test
    fun `keeps highest revision and first key order`() {
        val old = AssociateLatestEvent.Event(10, 1, "old")
        val other = AssociateLatestEvent.Event(20, 3, "other")
        val latest = AssociateLatestEvent.Event(10, 2, "latest")
        val stale = AssociateLatestEvent.Event(20, 1, "stale")

        assertThat(AssociateLatestEvent.index(listOf(old, other, latest, stale)).values)
            .containsExactly(latest, other)
    }

    @Test
    fun `handles empty input`() {
        assertThat(AssociateLatestEvent.index(emptyList())).isEmpty()
    }
}
