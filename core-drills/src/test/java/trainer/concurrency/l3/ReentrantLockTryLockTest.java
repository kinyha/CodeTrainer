package trainer.concurrency.l3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ReentrantLockTryLockTest {

    @Test
    void incrementsWhenLockIsFree() throws InterruptedException {
        ReentrantLockTryLock counter = new ReentrantLockTryLock();
        assertThat(counter.tryIncrement(1, TimeUnit.SECONDS)).isTrue();
        assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    void givesUpWhenLockIsHeldByAnotherThread() throws Exception {
        ReentrantLockTryLock counter = new ReentrantLockTryLock();
        CountDownLatch locked = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                counter.lock.lock();
                try {
                    locked.countDown();
                    release.await();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    counter.lock.unlock();
                }
            });

            assertThat(locked.await(1, TimeUnit.SECONDS)).isTrue();
            assertThat(counter.tryIncrement(50, TimeUnit.MILLISECONDS)).isFalse();
            release.countDown();
        }

        assertThat(counter.tryIncrement(1, TimeUnit.SECONDS)).isTrue();
        assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    void rejectsNullUnit() {
        assertThatNullPointerException().isThrownBy(() -> new ReentrantLockTryLock().tryIncrement(1, null));
    }
}
