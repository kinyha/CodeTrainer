package trainer.kafka.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BasicValueListenerTest {

    private final BasicValueListener.MessageProcessor processor = mock();
    private final BasicValueListener listener = new BasicValueListener(processor);

    @Test
    void forwardsPayloadToProcessor() {
        listener.onMessage("hello");
        verify(processor).process("hello");
    }

    @Test
    void rejectsNullPayload() {
        assertThatNullPointerException().isThrownBy(() -> listener.onMessage(null));
    }
}
