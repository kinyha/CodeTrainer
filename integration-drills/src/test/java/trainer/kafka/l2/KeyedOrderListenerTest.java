package trainer.kafka.l2;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class KeyedOrderListenerTest {

    private final KeyedOrderListener.OrderProcessor processor = mock();
    private final KeyedOrderListener listener = new KeyedOrderListener(processor);

    @Test
    void forwardsKeyAndPayload() {
        listener.onMessage(new ConsumerRecord<>(KeyedOrderListener.TOPIC, 0, 1, "order-7", "created"));
        verify(processor).process("order-7", "created");
    }

    @Test
    void rejectsMissingKey() {
        var record = new ConsumerRecord<String, String>(KeyedOrderListener.TOPIC, 0, 1, null, "created");
        assertThatNullPointerException().isThrownBy(() -> listener.onMessage(record)).withMessage("record key");
    }
}
