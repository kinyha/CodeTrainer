package trainer.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

public final class KafkaExerciseTestSupport {

    private KafkaExerciseTestSupport() {
    }

    public static ConsumerFactory<String, String> consumerFactory(EmbeddedKafkaBroker broker, String groupId) {
        Map<String, Object> properties = KafkaTestUtils.consumerProps(broker, groupId, false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), new StringDeserializer());
    }

    public static ConcurrentKafkaListenerContainerFactory<String, String> containerFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    public static ProducerFactory<String, String> producerFactory(EmbeddedKafkaBroker broker) {
        return new DefaultKafkaProducerFactory<>(
                KafkaTestUtils.producerProps(broker), new StringSerializer(), new StringSerializer());
    }

    public static KafkaTemplate<String, String> template(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
