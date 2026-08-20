package trainer.kafka.l3;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
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

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(IdempotentConsumerDedupEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = IdempotentConsumerDedup.TOPIC)
@DirtiesContext
class IdempotentConsumerDedupEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired RecordingProcessor processor;

    @Test
    void secondDeliveryOfTheSameEventIdIsIgnored() throws Exception {
        send("value-1", "dup-event");
        send("value-1-retry", "dup-event"); // тот же event-id, повторная доставка
        send("value-2", "other-event"); // сигнал "оба предыдущих уже долетели" — партиция одна, порядок сохранён

        assertThat(processor.thirdArrived.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(processor.callCount.get()).isEqualTo(2);
    }

    private void send(String value, String eventId) throws Exception {
        var record = new ProducerRecord<>(IdempotentConsumerDedup.TOPIC, null, "key", value);
        record.headers().add(new RecordHeader(
                IdempotentConsumerDedup.EVENT_ID_HEADER, eventId.getBytes(StandardCharsets.UTF_8)));
        template.send(record).get(10, TimeUnit.SECONDS);
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "idempotent-consumer-test");
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
        RecordingProcessor processor() {
            return new RecordingProcessor();
        }

        @Bean
        IdempotentConsumerDedup listener(RecordingProcessor processor) {
            return new IdempotentConsumerDedup(processor);
        }
    }

    static final class RecordingProcessor implements IdempotentConsumerDedup.EventProcessor {
        private final AtomicInteger callCount = new AtomicInteger();
        private final CountDownLatch thirdArrived = new CountDownLatch(1);

        @Override
        public void process(String payload) {
            callCount.incrementAndGet();
            if ("value-2".equals(payload)) {
                thirdArrived.countDown();
            }
        }
    }
}
