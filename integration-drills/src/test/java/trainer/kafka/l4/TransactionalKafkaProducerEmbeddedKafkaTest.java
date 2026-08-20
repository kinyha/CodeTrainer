package trainer.kafka.l4;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import trainer.kafka.KafkaExerciseTestSupport;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(TransactionalKafkaProducerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = {
        TransactionalKafkaProducer.REQUESTS_TOPIC, TransactionalKafkaProducer.CONFIRMATIONS_TOPIC
})
@DirtiesContext
class TransactionalKafkaProducerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> requestPublisher;
    @Autowired EmbeddedKafkaBroker broker;

    @Test
    void confirmationIsVisibleOnlyAfterTheTransactionCommits() throws Exception {
        requestPublisher.send(TransactionalKafkaProducer.REQUESTS_TOPIC, "order-9").get(10, TimeUnit.SECONDS);

        var consumerProps = KafkaTestUtils.consumerProps(broker, "confirmation-reader", true);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        try (var consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(
                consumerProps, new org.apache.kafka.common.serialization.StringDeserializer(),
                new org.apache.kafka.common.serialization.StringDeserializer())) {
            consumer.subscribe(List.of(TransactionalKafkaProducer.CONFIRMATIONS_TOPIC));

            var records = consumer.poll(Duration.ofSeconds(15));
            assertThat(records.count()).isEqualTo(1);
            var record = records.iterator().next();
            assertThat(record.key()).isEqualTo("order-9");
            assertThat(record.value()).isEqualTo("confirmed:order-9");
        }
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "transactional-order-test");
        }

        @Bean
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
                ConsumerFactory<String, String> factory) {
            return KafkaExerciseTestSupport.containerFactory(factory);
        }

        /** transactionIdPrefix превращает фабрику в транзакционную — без него executeInTransaction упал бы. */
        @Bean
        ProducerFactory<String, String> producerFactory(EmbeddedKafkaBroker broker) {
            var factory = new DefaultKafkaProducerFactory<>(
                    KafkaTestUtils.producerProps(broker), new StringSerializer(), new StringSerializer());
            factory.setTransactionIdPrefix("trainer-tx-");
            return factory;
        }

        @Bean
        KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> factory) {
            return KafkaExerciseTestSupport.template(factory);
        }

        @Bean
        TransactionalKafkaProducer listener(@Qualifier("kafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
            return new TransactionalKafkaProducer(kafkaTemplate);
        }

        /** Отдельный НЕ-транзакционный producer — имитирует внешнего клиента, публикующего запрос. */
        @Bean
        KafkaTemplate<String, String> requestPublisher(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.template(KafkaExerciseTestSupport.producerFactory(broker));
        }
    }
}
