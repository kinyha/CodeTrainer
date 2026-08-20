package trainer.kafka.l3;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class BatchListenerTest {

    private final BatchListener.OrderProcessor processor = mock();
    private final BatchListener listener = new BatchListener(processor);

    @Test
    void processesEveryRecordInTheBatch() {
        var records = List.of(
                new ConsumerRecord<>(BatchListener.TOPIC, 0, 1, "order-1", "created"),
                new ConsumerRecord<>(BatchListener.TOPIC, 0, 2, "order-2", "created"));

        listener.onMessages(records);

        verify(processor).process("order-1", "created");
        verify(processor).process("order-2", "created");
        verifyNoMoreInteractions(processor);
    }

    @Test
    void rejectsNullBatch() {
        assertThatNullPointerException().isThrownBy(() -> listener.onMessages(null));
    }
}
