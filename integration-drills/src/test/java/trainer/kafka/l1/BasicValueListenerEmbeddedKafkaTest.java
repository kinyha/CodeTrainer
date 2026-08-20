package trainer.kafka.l1;

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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(BasicValueListenerEmbeddedKafkaTest.Config.class)
@EmbeddedKafka(partitions = 1, topics = BasicValueListener.TOPIC)
@DirtiesContext
class BasicValueListenerEmbeddedKafkaTest {

    @Autowired KafkaTemplate<String, String> template;
    @Autowired RecordingProcessor processor;

    @Test
    void receivesPublishedValue() throws Exception {
        template.send(BasicValueListener.TOPIC, "hello-embedded").get(10, TimeUnit.SECONDS);
        assertThat(processor.received.await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(processor.value).isEqualTo("hello-embedded");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableKafka
    static class Config {
        @Bean
        ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker) {
            return KafkaExerciseTestSupport.consumerFactory(broker, "basic-value-test");
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
        BasicValueListener listener(RecordingProcessor processor) {
            return new BasicValueListener(processor);
        }
    }

    static final class RecordingProcessor implements BasicValueListener.MessageProcessor {
        private final CountDownLatch received = new CountDownLatch(1);
        private volatile String value;

        @Override
        public void process(String payload) {
            value = payload;
            received.countDown();
        }
    }
}
