package trainer.concurrency.l5;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

class TokenBucketRateLimiterTest {

    @Test
    void refillsFromInjectedMonotonicClockAndCapsBurst() {
        AtomicLong clock = new AtomicLong();
        var limiter = new TokenBucketRateLimiter(5, 2, clock::get);

        assertThat(limiter.tryAcquire(5)).isTrue();
        assertThat(limiter.tryAcquire(1)).isFalse();
        clock.addAndGet(500_000_000L);
        assertThat(limiter.tryAcquire(1)).isTrue();
        assertThat(limiter.tryAcquire(1)).isFalse();
        clock.addAndGet(10_000_000_000L);
        assertThat(limiter.tryAcquire(5)).isTrue();
        assertThat(limiter.tryAcquire(1)).isFalse();
    }

    @Test
    void concurrentCallersCannotOverspendBucket() throws Exception {
        var limiter = new TokenBucketRateLimiter(10, 1, () -> 0L);
        AtomicInteger allowed = new AtomicInteger();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = new ArrayList<java.util.concurrent.Future<?>>();
            for (int index = 0; index < 100; index++) {
                futures.add(executor.submit(() -> {
                    if (limiter.tryAcquire(1)) allowed.incrementAndGet();
                }));
            }
            for (var future : futures) future.get();
        }
        assertThat(allowed).hasValue(10);
    }
}
