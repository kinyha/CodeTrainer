package trainer.kotlinlang.l1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhenExpressionExhaustiveTest {

    @Test
    fun `maps each light to its action`() {
        assertThat(WhenExpressionExhaustive.action(WhenExpressionExhaustive.TrafficLight.Red)).isEqualTo("Stop")
        assertThat(WhenExpressionExhaustive.action(WhenExpressionExhaustive.TrafficLight.Yellow)).isEqualTo("Caution")
        assertThat(WhenExpressionExhaustive.action(WhenExpressionExhaustive.TrafficLight.Green)).isEqualTo("Go")
    }
}
