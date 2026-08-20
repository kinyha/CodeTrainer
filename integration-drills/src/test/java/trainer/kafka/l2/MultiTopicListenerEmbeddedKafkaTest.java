package trainer.kafka.l2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import trainer.kafka.KafkaExerciseTestSupport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(MultiTopicListenerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = {MultiTopicListener.ORDERS_TOPIC, MultiTopicListener.PAYMENTS_TOPIC})
@DirtiesContext
class MultiTopicListenerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired RecordingRouter router;

    @Test
    void routesMessagesFromBothTopics() throws Exception {
        template.send(MultiTopicListener.ORDERS_TOPIC, "order-created").get(10, TimeUnit.SECONDS);
        template.send(MultiTopicListener.PAYMENTS_TOPIC, "payment-captured").get(10, TimeUnit.SECONDS);

        assertThat(router.received.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(router.routed).containsEntry(MultiTopicListener.ORDERS_TOPIC, "order-created");
        assertThat(router.routed).containsEntry(MultiTopicListener.PAYMENTS_TOPIC, "payment-captured");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "multi-topic-test");
        }

        @Bean
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
                ConsumerFactory<String, String> factory) {
            return KafkaExerciseTestSupport.containerFactory(factory);
        }

        @Bean
        ProducerFactory<String, String> producerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.producerFactory(broker);
        }

        @Bean
        KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> factory) {
            return KafkaExerciseTestSupport.template(factory);
        }

        @Bean
        RecordingRouter router() {
            return new RecordingRouter();
        }

        @Bean
        MultiTopicListener listener(RecordingRouter router) {
            return new MultiTopicListener(router);
        }
    }

    static final class RecordingRouter implements MultiTopicListener.EventRouter {
        private final CountDownLatch received = new CountDownLatch(2);
        private final Map<String, String> routed = new ConcurrentHashMap<>();

        @Override
        public void route(String topic, String payload) {
            routed.put(topic, payload);
            received.countDown();
        }
    }
}
