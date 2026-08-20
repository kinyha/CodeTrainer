package trainer.kotlinlang.l2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OperatorOverloadingTest {

    @Test
    fun `adds two money values using the plus operator`() {
        val total = OperatorOverloading.Money(150) + OperatorOverloading.Money(250)
        assertThat(total).isEqualTo(OperatorOverloading.Money(400))
    }

    @Test
    fun `multiplies money by a factor`() {
        assertThat(OperatorOverloading.Money(100) * 3).isEqualTo(OperatorOverloading.Money(300))
    }
}
