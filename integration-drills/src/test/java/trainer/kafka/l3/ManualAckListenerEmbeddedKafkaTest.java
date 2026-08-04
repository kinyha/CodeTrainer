package trainer.kafka.l3;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(ManualAckListenerEmbeddedKafkaTest.KafkaTestConfiguration.class)
@EmbeddedKafka(partitions = 1, topics = ManualAckListener.TOPIC)
@DirtiesContext
class ManualAckListenerEmbeddedKafkaTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RecordingProcessor processor;

    @Test
    void receivesRecordWithRealAcknowledgment() throws Exception {
        kafkaTemplate.send(ManualAckListener.TOPIC, "order-99").get(10, TimeUnit.SECONDS);

        assertThat(processor.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(processor.lastOrderId()).isEqualTo("order-99");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class KafkaTestConfiguration {

        @Bean
        ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            Map<String, Object> properties = KafkaTestUtils.consumerProps(broker, "manual-ack-test", false);
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            return new DefaultKafkaConsumerFactory<>(
                    properties,
                    new StringDeserializer(),
                    new StringDeserializer()
            );
        }

        @Bean
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
                ConsumerFactory<String, String> consumerFactory
        ) {
            var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
            factory.setConsumerFactory(consumerFactory);
            return factory; // AckMode переопределяется атрибутом конкретного @KafkaListener
        }

        @Bean
        ProducerFactory<String, String> producerFactory(EmbeddedKafkaBroker broker) {
            return new DefaultKafkaProducerFactory<>(
                    KafkaTestUtils.producerProps(broker),
                    new StringSerializer(),
                    new StringSerializer()
            );
        }

        @Bean
        KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }

        @Bean
        RecordingProcessor processor() {
            return new RecordingProcessor();
        }

        @Bean
        ManualAckListener listener(RecordingProcessor processor) {
            return new ManualAckListener(processor);
        }
    }

    static final class RecordingProcessor implements ManualAckListener.OrderProcessor {
        private final CountDownLatch received = new CountDownLatch(1);
        private volatile String lastOrderId;

        @Override
        public void process(String orderId) {
            lastOrderId = orderId;
            received.countDown();
        }

        boolean await(long timeout, TimeUnit unit) throws InterruptedException {
            return received.await(timeout, unit);
        }

        String lastOrderId() {
            return lastOrderId;
        }
    }
}
