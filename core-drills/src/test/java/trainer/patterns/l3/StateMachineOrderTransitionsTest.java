package trainer.patterns.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class StateMachineOrderTransitionsTest {

    @Test
    void allowsAValidTransition() {
        assertThat(StateMachineOrderTransitions.transition(
                StateMachineOrderTransitions.Status.NEW, StateMachineOrderTransitions.Status.PAID))
                .isEqualTo(StateMachineOrderTransitions.Status.PAID);
    }

    @Test
    void rejectsSkippingAStage() {
        assertThatIllegalStateException().isThrownBy(() -> StateMachineOrderTransitions.transition(
                StateMachineOrderTransitions.Status.NEW, StateMachineOrderTransitions.Status.SHIPPED));
    }

    @Test
    void rejectsTransitionsOutOfATerminalState() {
        assertThatIllegalStateException().isThrownBy(() -> StateMachineOrderTransitions.transition(
                StateMachineOrderTransitions.Status.DELIVERED, StateMachineOrderTransitions.Status.CANCELLED));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> StateMachineOrderTransitions.transition(
                null, StateMachineOrderTransitions.Status.PAID));
    }
}
