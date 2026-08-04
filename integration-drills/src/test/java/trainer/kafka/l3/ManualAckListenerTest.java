package trainer.kafka.l3;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.kafka.support.Acknowledgment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ManualAckListenerTest {

    private final ManualAckListener.OrderProcessor processor = mock(ManualAckListener.OrderProcessor.class);
    private final Acknowledgment acknowledgment = mock(Acknowledgment.class);
    private final ManualAckListener listener = new ManualAckListener(processor);

    @Test
    void acknowledgesOnlyAfterSuccessfulProcessing() {
        listener.onMessage("order-42", acknowledgment);

        InOrder order = inOrder(processor, acknowledgment);
        order.verify(processor).process("order-42");
        order.verify(acknowledgment).acknowledge();
    }

    @Test
    void doesNotAcknowledgeFailedProcessing() {
        doThrow(new IllegalStateException("database unavailable"))
                .when(processor).process("order-42");

        assertThatThrownBy(() -> listener.onMessage("order-42", acknowledgment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("database unavailable");
        verify(acknowledgment, never()).acknowledge();
    }
}
