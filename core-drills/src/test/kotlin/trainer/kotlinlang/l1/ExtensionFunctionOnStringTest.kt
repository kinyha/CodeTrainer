package trainer.kotlinlang.l1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExtensionFunctionOnStringTest {

    @Test
    fun `keeps short strings untouched`() {
        assertThat("hi".truncate(5)).isEqualTo("hi")
    }

    @Test
    fun `truncates long strings and appends ellipsis`() {
        assertThat("hello world".truncate(5)).isEqualTo("hello…")
    }

    @Test
    fun `string exactly at the limit stays untouched`() {
        assertThat("hello".truncate(5)).isEqualTo("hello")
    }
}
