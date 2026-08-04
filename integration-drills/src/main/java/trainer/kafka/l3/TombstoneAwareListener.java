package trainer.kafka.l3;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Objects;

// @task kafka.l3.TombstoneAwareListener
// @tags kafka,compacted-topic,tombstone,null-payload
// @time 25m
// @src  new
public final class TombstoneAwareListener {

    public static final String TOPIC = "trainer.customers.compacted";
    private final CustomerStore store;

    public TombstoneAwareListener(CustomerStore store) {
        this.store = Objects.requireNonNull(store, "store");
    }

    /** null payload — tombstone: удалить состояние по ключу, иначе обновить. */
    @KafkaListener(id = "customer-tombstone-listener", topics = TOPIC)
    public void onMessage(ConsumerRecord<String, String> record) {
        Objects.requireNonNull(record, "record");

        // ---8<--- solution
        String customerId = Objects.requireNonNull(record.key(), "record key");
        if (record.value() == null) {
            store.delete(customerId);
        } else {
            store.upsert(customerId, record.value());
        }
        // --->8--- solution
    }

    public interface CustomerStore {
        void upsert(String customerId, String payload);

        void delete(String customerId);
    }
}
