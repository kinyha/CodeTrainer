package trainer.kafka.l4;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;

// @task kafka.l4.TransactionalKafkaProducer
// @tags kafka,transactional-producer,exactly-once,executeInTransaction
// @time 45m
// @src  new
public final class TransactionalKafkaProducer {

    public static final String REQUESTS_TOPIC = "trainer.orders.requests";
    public static final String CONFIRMATIONS_TOPIC = "trainer.orders.confirmations";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionalKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = Objects.requireNonNull(kafkaTemplate, "kafkaTemplate");
    }

    /**
     * executeInTransaction гарантирует: либо offset потребления и отправка в CONFIRMATIONS_TOPIC
     * коммитятся вместе, либо не коммитится ни то, ни другое. Упади код внутри блока —
     * транзакция откатится целиком, и брокер повторно доставит исходное сообщение.
     */
    @KafkaListener(id = "transactional-order-listener", topics = REQUESTS_TOPIC)
    public void onMessage(String orderId) {
        Objects.requireNonNull(orderId, "orderId");

        // ---8<--- solution
        kafkaTemplate.<Void>executeInTransaction(operations -> {
            operations.send(CONFIRMATIONS_TOPIC, orderId, "confirmed:" + orderId);
            return null;
        });
        // --->8--- solution
    }
}
