package trainer.kafka.l2;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Objects;

// @task kafka.l2.MultiTopicListener
// @tags kafka,multi-topic,received-topic,routing
// @time 15m
// @src  new
public final class MultiTopicListener {

    public static final String ORDERS_TOPIC = "trainer.multi.orders";
    public static final String PAYMENTS_TOPIC = "trainer.multi.payments";

    private final EventRouter router;

    public MultiTopicListener(EventRouter router) {
        this.router = Objects.requireNonNull(router, "router");
    }

    /** RECEIVED_TOPIC — служебный заголовок Spring Kafka: сообщает, пришло из ORDERS_TOPIC или PAYMENTS_TOPIC. */
    @KafkaListener(id = "multi-topic-listener", topics = {ORDERS_TOPIC, PAYMENTS_TOPIC})
    public void onMessage(String payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Objects.requireNonNull(payload, "payload");
        Objects.requireNonNull(topic, "topic");

        // ---8<--- solution
        router.route(topic, payload);
        // --->8--- solution
    }

    @FunctionalInterface
    public interface EventRouter {
        void route(String topic, String payload);
    }
}
