package trainer.kafka.l3;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
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

@SpringJUnitConfig(TombstoneAwareListenerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = TombstoneAwareListener.TOPIC)
@DirtiesContext
class TombstoneAwareListenerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired RecordingStore store;

    @Test
    void receivesUpsertAndTombstoneFromBroker() throws Exception {
        template.send(TombstoneAwareListener.TOPIC, "customer-2", "Bob").get(10, TimeUnit.SECONDS);
        template.send(new ProducerRecord<>(TombstoneAwareListener.TOPIC, "customer-2", null))
                .get(10, TimeUnit.SECONDS);

        assertThat(store.received.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(store.operations).isEqualTo("upsert:customer-2:Bob|delete:customer-2");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        org.springframework.kafka.core.ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "tombstone-test");
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

        @Bean RecordingStore store() { return new RecordingStore(); }
        @Bean TombstoneAwareListener listener(RecordingStore store) { return new TombstoneAwareListener(store); }
    }

    static final class RecordingStore implements TombstoneAwareListener.CustomerStore {
        private final CountDownLatch received = new CountDownLatch(2);
        private volatile String operations = "";

        @Override public synchronized void upsert(String id, String payload) {
            operations = "upsert:" + id + ":" + payload;
            received.countDown();
        }

        @Override public synchronized void delete(String id) {
            operations += "|delete:" + id;
            received.countDown();
        }
    }
}
