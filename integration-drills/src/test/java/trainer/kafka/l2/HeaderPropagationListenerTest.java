package trainer.kafka.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HeaderPropagationListenerTest {

    private final HeaderPropagationListener.MessageProcessor processor = mock();
    private final HeaderPropagationListener listener = new HeaderPropagationListener(processor);

    @Test
    void forwardsPayloadAndCorrelationId() {
        listener.onMessage("hello", "trace-1");
        verify(processor).process("trace-1", "hello");
    }

    @Test
    void rejectsMissingCorrelationId() {
        assertThatNullPointerException().isThrownBy(() -> listener.onMessage("hello", null));
    }
}
