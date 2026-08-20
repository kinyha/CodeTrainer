package trainer.patterns.l4;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ProxyLazyInitializationTest {

    @Test
    void createsTheDelegateOnlyOnce() {
        AtomicInteger creations = new AtomicInteger();
        ProxyLazyInitialization<String> proxy = new ProxyLazyInitialization<>(() -> {
            creations.incrementAndGet();
            return "expensive-resource";
        });

        assertThat(proxy.get()).isEqualTo("expensive-resource");
        assertThat(proxy.get()).isEqualTo("expensive-resource");
        assertThat(creations.get()).isEqualTo(1);
    }

    @Test
    void neverCreatesTwiceUnderConcurrentFirstAccess() throws InterruptedException {
        AtomicInteger creations = new AtomicInteger();
        ProxyLazyInitialization<Object> proxy = new ProxyLazyInitialization<>(() -> {
            creations.incrementAndGet();
            return new Object();
        });

        int threads = 100;
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch go = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    ready.countDown();
                    try {
                        go.await();
                        proxy.get();
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

        assertThat(creations.get()).isEqualTo(1);
    }
}
