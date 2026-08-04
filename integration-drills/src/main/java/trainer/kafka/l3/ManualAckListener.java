package trainer.kafka.l3;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Objects;

// @task kafka.l3.ManualAckListener
// @tags kafka,consumer,manual-ack,offset,at-least-once
// @time 30m
// @src  new
// @doc  ManualAckListener.md
public final class ManualAckListener {

    public static final String TOPIC = "trainer.orders.created";
    private final OrderProcessor processor;

    public ManualAckListener(OrderProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "processor");
    }

    /**
     * Обрабатывает идентификатор заказа и подтверждает offset только после успеха.
     * Контракт: если processor бросает исключение, acknowledge не вызывается.
     */
    @KafkaListener(
            id = "manual-ack-orders",
            topics = TOPIC,
            ackMode = "MANUAL_IMMEDIATE"
    )
    public void onMessage(String orderId, Acknowledgment acknowledgment) {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(acknowledgment, "acknowledgment");

        // ---8<--- solution
        processor.process(orderId);
        acknowledgment.acknowledge(); // WHY: commit до process потеряет сообщение при ошибке бизнес-логики
        // --->8--- solution
    }

    @FunctionalInterface
    public interface OrderProcessor {
        void process(String orderId);
    }
}
