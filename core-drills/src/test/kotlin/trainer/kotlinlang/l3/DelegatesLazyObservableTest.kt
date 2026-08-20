package trainer.kotlinlang.l3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DelegatesLazyObservableTest {

    @Test
    fun `computes rtl default theme for arabic locale`() {
        val settings = DelegatesLazyObservable.Settings("ar-SA")
        assertThat(settings.defaultTheme).isEqualTo("dark-rtl")
    }

    @Test
    fun `computes light default theme for other locales`() {
        val settings = DelegatesLazyObservable.Settings("en-US")
        assertThat(settings.defaultTheme).isEqualTo("light")
    }

    @Test
    fun `observable callback fires with old and new value on change`() {
        val settings = DelegatesLazyObservable.Settings("en-US")
        var recordedOld: String? = null
        var recordedNew: String? = null
        settings.onThemeChanged = { old, new -> recordedOld = old; recordedNew = new }

        settings.theme = "dark"

        assertThat(recordedOld).isEqualTo("light")
        assertThat(recordedNew).isEqualTo("dark")
    }
}
