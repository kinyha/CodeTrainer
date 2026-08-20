package trainer.concurrency.l4;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class DoubleCheckedSingletonTest {

    @Test
    void alwaysReturnsTheSameInstance() {
        DoubleCheckedSingleton first = DoubleCheckedSingleton.getInstance();
        DoubleCheckedSingleton second = DoubleCheckedSingleton.getInstance();
        assertThat(first).isSameAs(second);
    }

    @Test
    void concurrentFirstAccessNeverConstructsTwoInstances() throws Exception {
        resetSingleton();

        int threads = 100;
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch go = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        Set<DoubleCheckedSingleton> seen = ConcurrentHashMap.newKeySet();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    ready.countDown();
                    try {
                        go.await();
                        seen.add(DoubleCheckedSingleton.getInstance());
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

    /** Сбрасывает статический singleton, чтобы тест гонки честно проверял ПЕРВОЕ обращение. */
    private static void resetSingleton() throws Exception {
        Field field = DoubleCheckedSingleton.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }
}
