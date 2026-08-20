package trainer.kafka.l2;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Objects;

// @task kafka.l2.HeaderPropagationListener
// @tags kafka,header,correlation-id,metadata
// @time 15m
// @src  new
public final class HeaderPropagationListener {

    public static final String TOPIC = "trainer.notifications.correlated";
    public static final String CORRELATION_ID_HEADER = "correlation-id";

    private final MessageProcessor processor;

    public HeaderPropagationListener(MessageProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "processor");
    }

    /** @Header достаёт заголовок по имени — не нужно вручную искать его в ConsumerRecord.headers(). */
    @KafkaListener(id = "header-propagation-listener", topics = TOPIC)
    public void onMessage(String payload, @Header(CORRELATION_ID_HEADER) String correlationId) {
        Objects.requireNonNull(payload, "payload");
        Objects.requireNonNull(correlationId, "correlationId");

        // ---8<--- solution
        processor.process(correlationId, payload);
        // --->8--- solution
    }

    @FunctionalInterface
    public interface MessageProcessor {
        void process(String correlationId, String payload);
    }
}
