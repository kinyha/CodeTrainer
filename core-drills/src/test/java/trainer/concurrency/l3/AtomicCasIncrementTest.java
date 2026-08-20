package trainer.concurrency.l3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class AtomicCasIncrementTest {

    @Test
    void incrementsUntilCeiling() {
        AtomicCasIncrement counter = new AtomicCasIncrement();
        assertThat(counter.incrementIfBelow(2)).isEqualTo(1);
        assertThat(counter.incrementIfBelow(2)).isEqualTo(2);
        assertThat(counter.incrementIfBelow(2)).isEqualTo(2);
    }

    @Test
    void neverExceedsCeilingUnderConcurrentContention() throws InterruptedException {
        AtomicCasIncrement counter = new AtomicCasIncrement();
        int ceiling = 100;
        int threads = 50;
        CountDownLatch done = new CountDownLatch(threads);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < ceiling; j++) {
                        counter.incrementIfBelow(ceiling);
                    }
                    done.countDown();
                });
            }
            assertThat(done.await(5, TimeUnit.SECONDS)).isTrue();
        }
        assertThat(counter.get()).isEqualTo(ceiling);
    }
}
