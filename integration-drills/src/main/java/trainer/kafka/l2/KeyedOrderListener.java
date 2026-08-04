package trainer.kafka.l2;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Objects;

// @task kafka.l2.KeyedOrderListener
// @tags kafka,consumer-record,key,listener
// @time 20m
// @src  new
public final class KeyedOrderListener {

    public static final String TOPIC = "trainer.orders.keyed";
    private final OrderProcessor processor;

    public KeyedOrderListener(OrderProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "processor");
    }

    /** Передаёт бизнес-логике и ключ партиционирования, и payload. */
    @KafkaListener(id = "keyed-order-listener", topics = TOPIC)
    public void onMessage(ConsumerRecord<String, String> record) {
        Objects.requireNonNull(record, "record");

        // ---8<--- solution
        String orderId = Objects.requireNonNull(record.key(), "record key");
        String payload = Objects.requireNonNull(record.value(), "record value");
        processor.process(orderId, payload);
        // --->8--- solution
    }

    @FunctionalInterface
    public interface OrderProcessor {
        void process(String orderId, String payload);
    }
}
