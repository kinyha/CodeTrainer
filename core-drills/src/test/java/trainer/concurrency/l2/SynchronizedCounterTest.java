package trainer.concurrency.l2;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class SynchronizedCounterTest {

    @Test
    void incrementsSequentially() {
        SynchronizedCounter counter = new SynchronizedCounter();
        assertThat(counter.incrementAndGet()).isEqualTo(1);
        assertThat(counter.incrementAndGet()).isEqualTo(2);
    }

    @Test
    void survivesConcurrentIncrementsWithoutLostUpdates() throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        int threads = 50;
        int incrementsPerThread = 200;
        CountDownLatch done = new CountDownLatch(threads);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        counter.incrementAndGet();
                    }
                    done.countDown();
                });
            }
            assertThat(done.await(5, TimeUnit.SECONDS)).isTrue();
        }
        assertThat(counter.get()).isEqualTo((long) threads * incrementsPerThread);
    }
}
