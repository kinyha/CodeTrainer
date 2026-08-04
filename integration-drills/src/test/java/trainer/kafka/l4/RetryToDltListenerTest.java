package trainer.kafka.l4;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RetryToDltListenerTest {

    private final RetryToDltListener.EventProcessor processor = mock();
    private final RetryToDltListener.DltStore dltStore = mock();
    private final RetryToDltListener listener = new RetryToDltListener(processor, dltStore);

    @Test
    void delegatesMainAndDltPaths() {
        listener.onMessage("event-1");
        listener.onDlt("event-2");

        verify(processor).process("event-1");
        verify(dltStore).save("event-2");
    }
}
