package trainer.concurrency.l4;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ConcurrentHashMapAtomicComposeTest {

    @Test
    void incrementsSequentially() {
        ConcurrentHashMapAtomicCompose counts = new ConcurrentHashMapAtomicCompose();
        assertThat(counts.incrementAndGet("a")).isEqualTo(1);
        assertThat(counts.incrementAndGet("a")).isEqualTo(2);
        assertThat(counts.incrementAndGet("b")).isEqualTo(1);
    }

    @Test
    void neverLosesUpdatesUnderConcurrentIncrementsOnTheSameKey() throws InterruptedException {
        ConcurrentHashMapAtomicCompose counts = new ConcurrentHashMapAtomicCompose();
        int threads = 50;
        int incrementsPerThread = 200;
        CountDownLatch done = new CountDownLatch(threads);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        counts.incrementAndGet("shared");
                    }
                    done.countDown();
                });
            }
            assertThat(done.await(5, TimeUnit.SECONDS)).isTrue();
        }
        assertThat(counts.get("shared")).isEqualTo(threads * incrementsPerThread);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> new ConcurrentHashMapAtomicCompose().incrementAndGet(null));
    }
}
