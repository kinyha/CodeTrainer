package trainer.kotlinlang.l1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NullSafetyChainTest {

    @Test
    fun `returns the city when the whole chain is present`() {
        val order = NullSafetyChain.Order(NullSafetyChain.Customer(NullSafetyChain.Address("Berlin")))
        assertThat(NullSafetyChain.cityOrUnknown(order)).isEqualTo("Berlin")
    }

    @Test
    fun `falls back to Unknown when the order itself is null`() {
        assertThat(NullSafetyChain.cityOrUnknown(null)).isEqualTo("Unknown")
    }

    @Test
    fun `falls back to Unknown when a link in the middle of the chain is null`() {
        val order = NullSafetyChain.Order(NullSafetyChain.Customer(null))
        assertThat(NullSafetyChain.cityOrUnknown(order)).isEqualTo("Unknown")
    }

    @Test
    fun `falls back to Unknown when only the city itself is null`() {
        val order = NullSafetyChain.Order(NullSafetyChain.Customer(NullSafetyChain.Address(null)))
        assertThat(NullSafetyChain.cityOrUnknown(order)).isEqualTo("Unknown")
    }
}
