package trainer.kafka.l4;

import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;

import java.util.Objects;

// @task kafka.l4.RetryToDltListener
// @tags kafka,retry-topic,dlt,backoff,at-least-once
// @time 50m
// @src  new
// @doc  RetryToDltListener.md
public final class RetryToDltListener {

    public static final String TOPIC = "trainer.payments.retry";
    private final EventProcessor processor;
    private final DltStore dltStore;

    public RetryToDltListener(EventProcessor processor, DltStore dltStore) {
        this.processor = Objects.requireNonNull(processor, "processor");
        this.dltStore = Objects.requireNonNull(dltStore, "dltStore");
    }

    /** Три неблокирующие попытки, затем запись события из DLT handler. */
    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(delay = 100),
            kafkaTemplate = "kafkaTemplate",
            autoCreateTopics = "true",
            numPartitions = "1",
            replicationFactor = "1"
    )
    @KafkaListener(id = "retry-payment-listener", topics = TOPIC)
    public void onMessage(String eventId) {
        Objects.requireNonNull(eventId, "eventId");

        // ---8<--- solution
        processor.process(eventId);
        // --->8--- solution
    }

    @DltHandler
    public void onDlt(String eventId) {
        // ---8<--- solution
        dltStore.save(eventId);
        // --->8--- solution
    }

    @FunctionalInterface
    public interface EventProcessor {
        void process(String eventId);
    }

    @FunctionalInterface
    public interface DltStore {
        void save(String eventId);
    }
}
