package trainer.concurrency.l3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BoundedBufferWaitNotifyTest {

    @Test
    void producerWaitsForCapacityAndResumesAfterTake() throws Exception {
        BoundedBufferWaitNotify<Integer> buffer = new BoundedBufferWaitNotify<>(1);
        buffer.put(1);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var producer = executor.submit(() -> {
                buffer.put(2);
                return null;
            });

            assertThatExceptionOfType(TimeoutException.class)
                    .isThrownBy(() -> producer.get(50, TimeUnit.MILLISECONDS));
            assertThat(buffer.take()).isEqualTo(1);
            producer.get(1, TimeUnit.SECONDS);
            assertThat(buffer.take()).isEqualTo(2);
            assertThat(buffer.size()).isZero();
        }
    }

    @Test
    void rejectsInvalidCapacity() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BoundedBufferWaitNotify<>(0));
    }
}
