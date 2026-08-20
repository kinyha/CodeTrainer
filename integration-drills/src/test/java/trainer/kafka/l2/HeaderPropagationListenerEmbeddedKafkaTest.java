package trainer.kafka.l2;

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

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(HeaderPropagationListenerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = HeaderPropagationListener.TOPIC)
@DirtiesContext
class HeaderPropagationListenerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired RecordingProcessor processor;

    @Test
    void receivesPayloadWithCorrelationHeader() throws Exception {
        var record = new ProducerRecord<>(HeaderPropagationListener.TOPIC, null, "value-1", "hello-embedded");
        record.headers().add(new RecordHeader(
                HeaderPropagationListener.CORRELATION_ID_HEADER, "trace-42".getBytes(StandardCharsets.UTF_8)));

        template.send(record).get(10, TimeUnit.SECONDS);

        assertThat(processor.received.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(processor.correlationId).isEqualTo("trace-42");
        assertThat(processor.payload).isEqualTo("hello-embedded");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "header-propagation-test");
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
        HeaderPropagationListener listener(RecordingProcessor processor) {
            return new HeaderPropagationListener(processor);
        }
    }

    static final class RecordingProcessor implements HeaderPropagationListener.MessageProcessor {
        private final CountDownLatch received = new CountDownLatch(1);
        private volatile String correlationId;
        private volatile String payload;

        @Override
        public void process(String correlationId, String payload) {
            this.correlationId = correlationId;
            this.payload = payload;
            received.countDown();
        }
    }
}
