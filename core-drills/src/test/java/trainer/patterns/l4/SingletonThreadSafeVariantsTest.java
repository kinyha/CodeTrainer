package trainer.patterns.l4;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class SingletonThreadSafeVariantsTest {

    @Test
    void alwaysReturnsTheSameInstance() {
        assertThat(SingletonThreadSafeVariants.getInstance()).isSameAs(SingletonThreadSafeVariants.getInstance());
    }

    @Test
    void concurrentAccessNeverProducesTwoInstances() throws InterruptedException {
        int threads = 100;
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch go = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        Set<SingletonThreadSafeVariants> seen = ConcurrentHashMap.newKeySet();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    ready.countDown();
                    try {
                        go.await();
                        seen.add(SingletonThreadSafeVariants.getInstance());
                    } catch (InterruptedException error) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }
                });
            }
            assertThat(ready.await(2, TimeUnit.SECONDS)).isTrue();
            go.countDown();
            assertThat(done.await(2, TimeUnit.SECONDS)).isTrue();
        }

        assertThat(seen).hasSize(1);
    }
}
