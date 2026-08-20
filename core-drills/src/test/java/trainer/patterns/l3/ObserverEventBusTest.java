package trainer.patterns.l3;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ObserverEventBusTest {

    @Test
    void notifiesAllSubscribersOfEachEvent() {
        ObserverEventBus<String> bus = new ObserverEventBus<>();
        List<String> firstLog = new ArrayList<>();
        List<String> secondLog = new ArrayList<>();
        bus.subscribe(firstLog::add);
        bus.subscribe(secondLog::add);

        bus.publish("started");
        bus.publish("finished");

        assertThat(firstLog).containsExactly("started", "finished");
        assertThat(secondLog).containsExactly("started", "finished");
    }

    @Test
    void publishingWithNoSubscribersIsANoOp() {
        ObserverEventBus<String> bus = new ObserverEventBus<>();
        bus.publish("ignored"); // не должно бросить исключение
    }

    @Test
    void rejectsNull() {
        ObserverEventBus<String> bus = new ObserverEventBus<>();
        assertThatNullPointerException().isThrownBy(() -> bus.subscribe(null));
        assertThatNullPointerException().isThrownBy(() -> bus.publish(null));
    }
}
