package trainer.kafka.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MultiTopicListenerTest {

    private final MultiTopicListener.EventRouter router = mock();
    private final MultiTopicListener listener = new MultiTopicListener(router);

    @Test
    void routesByReceivedTopic() {
        listener.onMessage("payload-1", MultiTopicListener.ORDERS_TOPIC);
        verify(router).route(MultiTopicListener.ORDERS_TOPIC, "payload-1");
    }

    @Test
    void rejectsMissingTopic() {
        assertThatNullPointerException().isThrownBy(() -> listener.onMessage("payload", null));
    }
}
