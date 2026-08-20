package trainer.kafka.l3;

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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(BatchListenerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = BatchListener.TOPIC)
@DirtiesContext
class BatchListenerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired RecordingProcessor processor;

    @Test
    void receivesAllPublishedRecords() throws Exception {
        template.send(BatchListener.TOPIC, "order-1", "created").get(10, TimeUnit.SECONDS);
        template.send(BatchListener.TOPIC, "order-2", "created").get(10, TimeUnit.SECONDS);

        assertThat(processor.received.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(processor.seen).contains("order-1:created", "order-2:created");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "batch-listener-test");
        }

        @Bean
        ConcurrentKafkaListenerContainerFactory<String, String> batchListenerContainerFactory(
                ConsumerFactory<String, String> factory) {
            var containerFactory = KafkaExerciseTestSupport.containerFactory(factory);
            containerFactory.setBatchListener(true);
            return containerFactory;
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
        RecordingProcessor processor() {
            return new RecordingProcessor();
        }

        @Bean
        BatchListener listener(RecordingProcessor processor) {
            return new BatchListener(processor);
        }
    }

    static final class RecordingProcessor implements BatchListener.OrderProcessor {
        private final CountDownLatch received = new CountDownLatch(2);
        private final ConcurrentLinkedQueue<String> seen = new ConcurrentLinkedQueue<>();

        @Override
        public void process(String orderId, String payload) {
            seen.add(orderId + ":" + payload);
            received.countDown();
        }
    }
}
