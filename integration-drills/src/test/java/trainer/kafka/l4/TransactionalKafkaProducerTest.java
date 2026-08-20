package trainer.kafka.l4;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionalKafkaProducerTest {

    @SuppressWarnings("unchecked")
    private final KafkaTemplate<String, String> kafkaTemplate = mock();
    private final TransactionalKafkaProducer listener = new TransactionalKafkaProducer(kafkaTemplate);

    @Test
    void sendsConfirmationInsideATransaction() {
        when(kafkaTemplate.executeInTransaction(any())).thenAnswer(invocation -> {
            KafkaOperations.OperationsCallback<String, String, Void> callback = invocation.getArgument(0);
            return callback.doInOperations(kafkaTemplate);
        });

        listener.onMessage("order-7");

        verify(kafkaTemplate).send(TransactionalKafkaProducer.CONFIRMATIONS_TOPIC, "order-7", "confirmed:order-7");
    }

    @Test
    void rejectsNullOrderId() {
        assertThatNullPointerException().isThrownBy(() -> listener.onMessage(null));
    }
}
