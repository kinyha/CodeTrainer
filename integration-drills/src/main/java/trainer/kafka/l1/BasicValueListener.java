package trainer.kafka.l1;

import org.springframework.kafka.annotation.KafkaListener;

import java.util.Objects;

// @task kafka.l1.BasicValueListener
// @tags kafka,consumer,listener,value
// @time 10m
// @src  new
public final class BasicValueListener {

    public static final String TOPIC = "trainer.notifications.basic";
    private final MessageProcessor processor;

    public BasicValueListener(MessageProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "processor");
    }

    /** Простейший listener: Spring сам десериализует value в String, ключ и метаданные не нужны. */
    @KafkaListener(id = "basic-value-listener", topics = TOPIC)
    public void onMessage(String payload) {
        Objects.requireNonNull(payload, "payload");

        // ---8<--- solution
        processor.process(payload);
        // --->8--- solution
    }

    @FunctionalInterface
    public interface MessageProcessor {
        void process(String payload);
    }
}
