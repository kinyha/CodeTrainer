package trainer.kafka.l3;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class TombstoneAwareListenerTest {

    private final TombstoneAwareListener.CustomerStore store = mock();
    private final TombstoneAwareListener listener = new TombstoneAwareListener(store);

    @Test
    void upsertsRegularRecord() {
        listener.onMessage(record("customer-1", "Ada"));
        verify(store).upsert("customer-1", "Ada");
        verify(store, never()).delete("customer-1");
    }

    @Test
    void deletesOnTombstone() {
        listener.onMessage(record("customer-1", null));
        verify(store).delete("customer-1");
        verify(store, never()).upsert("customer-1", null);
    }

    private static ConsumerRecord<String, String> record(String key, String value) {
        return new ConsumerRecord<>(TombstoneAwareListener.TOPIC, 0, 1, key, value);
    }
}
