package trainer.kotlinlang.l4

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class InlineValueClassWrapperTest {

    @Test
    fun `describes a user and order pair`() {
        val result = InlineValueClassWrapper.describe(InlineValueClassWrapper.UserId(7), InlineValueClassWrapper.OrderId(99))
        assertThat(result).isEqualTo("user 7 placed order 99")
    }

    @Test
    fun `rejects non positive user id`() {
        assertThatThrownBy { InlineValueClassWrapper.UserId(0) }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
