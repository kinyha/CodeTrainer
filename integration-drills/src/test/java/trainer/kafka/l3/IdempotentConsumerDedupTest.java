package trainer.kafka.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class IdempotentConsumerDedupTest {

    private final IdempotentConsumerDedup.EventProcessor processor = mock();
    private final IdempotentConsumerDedup listener = new IdempotentConsumerDedup(processor);

    @Test
    void processesEachEventIdOnlyOnce() {
        listener.onMessage("payload", "event-1");
        listener.onMessage("payload", "event-1"); // redelivery того же event-id

        verify(processor, times(1)).process("payload");
    }

    @Test
    void processesDifferentEventIdsSeparately() {
        listener.onMessage("a", "event-1");
        listener.onMessage("b", "event-2");

        verify(processor).process("a");
        verify(processor).process("b");
    }

    @Test
    void rejectsMissingEventId() {
        assertThatNullPointerException().isThrownBy(() -> listener.onMessage("payload", null));
    }
}
