package trainer.kafka.l2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import trainer.kafka.KafkaExerciseTestSupport;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

@SpringJUnitConfig(KeyedOrderListenerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = KeyedOrderListener.TOPIC)
@DirtiesContext
class KeyedOrderListenerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired RecordingProcessor processor;

    @Test
    void receivesKafkaKeyAndValue() throws Exception {
        template.send(KeyedOrderListener.TOPIC, "order-8", "paid").get(10, TimeUnit.SECONDS);
        assertThat(processor.received.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(processor.value).isEqualTo("order-8:paid");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        org.springframework.kafka.core.ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "keyed-order-test");
        }

        @Bean
        org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory<String, String>
        kafkaListenerContainerFactory(org.springframework.kafka.core.ConsumerFactory<String, String> factory) {
            return KafkaExerciseTestSupport.containerFactory(factory);
        }

        @Bean
        org.springframework.kafka.core.ProducerFactory<String, String> producerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.producerFactory(broker);
        }

        @Bean KafkaTemplate<String, String> kafkaTemplate(
                org.springframework.kafka.core.ProducerFactory<String, String> factory) {
            return KafkaExerciseTestSupport.template(factory);
        }

        @Bean RecordingProcessor processor() { return new RecordingProcessor(); }
        @Bean KeyedOrderListener listener(RecordingProcessor processor) { return new KeyedOrderListener(processor); }
    }

    static final class RecordingProcessor implements KeyedOrderListener.OrderProcessor {
        private final CountDownLatch received = new CountDownLatch(1);
        private volatile String value;

        @Override public void process(String orderId, String payload) {
            value = orderId + ":" + payload;
            received.countDown();
        }
    }
}
