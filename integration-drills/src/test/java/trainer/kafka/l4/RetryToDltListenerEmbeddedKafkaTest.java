package trainer.kafka.l4;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import trainer.kafka.KafkaExerciseTestSupport;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(RetryToDltListenerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = RetryToDltListener.TOPIC)
@DirtiesContext
class RetryToDltListenerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired FailingProcessor processor;
    @Autowired RecordingDltStore dltStore;

    @Test
    void retriesThreeTimesThenInvokesDltHandler() throws Exception {
        template.send(RetryToDltListener.TOPIC, "event-9").get(10, TimeUnit.SECONDS);

        assertThat(dltStore.received.await(20, TimeUnit.SECONDS)).isTrue();
        assertThat(processor.attempts).hasValue(3);
        assertThat(dltStore.eventId).isEqualTo("event-9");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafkaRetryTopic
    static class Config {
        @Bean
        KafkaAdmin kafkaAdmin(EmbeddedKafkaBroker broker) {
            return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString()));
        }

        @Bean
        ThreadPoolTaskScheduler taskScheduler() {
            var scheduler = new ThreadPoolTaskScheduler();
            scheduler.setThreadNamePrefix("retry-topic-");
            return scheduler;
        }

        @Bean
        org.springframework.kafka.core.ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "retry-dlt-test");
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

        @Bean FailingProcessor processor() { return new FailingProcessor(); }
        @Bean RecordingDltStore dltStore() { return new RecordingDltStore(); }
        @Bean RetryToDltListener listener(FailingProcessor processor, RecordingDltStore store) {
            return new RetryToDltListener(processor, store);
        }
    }

    static final class FailingProcessor implements RetryToDltListener.EventProcessor {
        private final AtomicInteger attempts = new AtomicInteger();

        @Override public void process(String eventId) {
            attempts.incrementAndGet();
            throw new IllegalStateException("database unavailable");
        }
    }

    static final class RecordingDltStore implements RetryToDltListener.DltStore {
        private final CountDownLatch received = new CountDownLatch(1);
        private volatile String eventId;

        @Override public void save(String eventId) {
            this.eventId = eventId;
            received.countDown();
        }
    }
}
