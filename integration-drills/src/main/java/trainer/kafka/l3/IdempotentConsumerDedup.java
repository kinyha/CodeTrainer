package trainer.kafka.l3;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// @task kafka.l3.IdempotentConsumerDedup
// @tags kafka,idempotent-consumer,deduplication,at-least-once
// @time 25m
// @src  new
public final class IdempotentConsumerDedup {

    public static final String TOPIC = "trainer.events.deduped";
    public static final String EVENT_ID_HEADER = "event-id";

    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();
    private final EventProcessor processor;

    public IdempotentConsumerDedup(EventProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "processor");
    }

    /**
     * At-least-once доставка Kafka означает, что одно и то же сообщение может прийти повторно
     * (retry консьюмера, ребаланс). event-id из заголовка — способ узнать "это уже обрабатывали".
     */
    @KafkaListener(id = "idempotent-consumer-listener", topics = TOPIC)
    public void onMessage(String payload, @Header(EVENT_ID_HEADER) String eventId) {
        Objects.requireNonNull(payload, "payload");
        Objects.requireNonNull(eventId, "eventId");

        // ---8<--- solution
        if (processedEventIds.add(eventId)) {
            processor.process(payload);
        }
        // --->8--- solution
    }

    @FunctionalInterface
    public interface EventProcessor {
        void process(String payload);
    }
}
