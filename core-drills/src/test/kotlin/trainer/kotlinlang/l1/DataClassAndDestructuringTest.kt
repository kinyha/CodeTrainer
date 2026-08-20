package trainer.kotlinlang.l1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataClassAndDestructuringTest {

    @Test
    fun `swaps x and y via destructuring`() {
        assertThat(DataClassAndDestructuring.swapped(DataClassAndDestructuring.Point(1, 2)))
            .isEqualTo(DataClassAndDestructuring.Point(2, 1))
    }

    @Test
    fun `symmetric point stays the same`() {
        assertThat(DataClassAndDestructuring.swapped(DataClassAndDestructuring.Point(5, 5)))
            .isEqualTo(DataClassAndDestructuring.Point(5, 5))
    }
}
