package trainer.concurrency.l5;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BackpressureExecutorTest {

    @Test
    void blocksSubmitterUntilInFlightTaskCompletes() throws Exception {
        CountDownLatch firstStarted = new CountDownLatch(1);
        CountDownLatch releaseFirst = new CountDownLatch(1);
        CountDownLatch secondDone = new CountDownLatch(1);
        try (var delegate = Executors.newVirtualThreadPerTaskExecutor();
             var callers = Executors.newVirtualThreadPerTaskExecutor()) {
            var executor = new BackpressureExecutor(delegate, 1);
            executor.submit(() -> {
                firstStarted.countDown();
                try {
                    releaseFirst.await();
                } catch (InterruptedException error) {
                    Thread.currentThread().interrupt();
                }
            });
            assertThat(firstStarted.await(1, TimeUnit.SECONDS)).isTrue();

            var blockedSubmit = callers.submit(() -> {
                executor.submit(secondDone::countDown);
                return null;
            });
            assertThatExceptionOfType(TimeoutException.class)
                    .isThrownBy(() -> blockedSubmit.get(50, TimeUnit.MILLISECONDS));
            releaseFirst.countDown();
            blockedSubmit.get(1, TimeUnit.SECONDS);
            assertThat(secondDone.await(1, TimeUnit.SECONDS)).isTrue();
        }
    }

    @Test
    void restoresPermitWhenDelegateRejectsTask() {
        var executor = new BackpressureExecutor(command -> {
            throw new RejectedExecutionException("closed");
        }, 1);

        assertThatExceptionOfType(RejectedExecutionException.class)
                .isThrownBy(() -> executor.submit(() -> {}));
        assertThat(executor.availableSlots()).isEqualTo(1);
    }
}
