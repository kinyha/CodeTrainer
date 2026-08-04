package trainer.concurrency.l5;

import java.util.Objects;
import java.util.function.LongSupplier;

// @task concurrency.l5.TokenBucketRateLimiter
// @tags concurrency,rate-limiter,token-bucket,clock,thread-safe
// @time 70m
// @src  new
// @doc  TokenBucketRateLimiter.md
public final class TokenBucketRateLimiter {

    private static final double NANOS_PER_SECOND = 1_000_000_000.0;
    private final int capacity;
    private final double refillPerSecond;
    private final LongSupplier nanoTime;
    private double tokens;
    private long lastRefillNanos;

    public TokenBucketRateLimiter(int capacity, double refillPerSecond, LongSupplier nanoTime) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        if (!Double.isFinite(refillPerSecond) || refillPerSecond <= 0) {
            throw new IllegalArgumentException("refillPerSecond must be positive and finite");
        }
        this.capacity = capacity;
        this.refillPerSecond = refillPerSecond;
        this.nanoTime = Objects.requireNonNull(nanoTime, "nanoTime");
        this.tokens = capacity;
        this.lastRefillNanos = nanoTime.getAsLong();
    }

    /** Атомарно пополняет bucket по монотонным часам и пытается забрать permits. */
    public synchronized boolean tryAcquire(int permits) {
        if (permits <= 0 || permits > capacity) throw new IllegalArgumentException("invalid permits");

        // ---8<--- solution
        long now = nanoTime.getAsLong();
        long elapsed = Math.max(0, now - lastRefillNanos);
        tokens = Math.min(capacity, tokens + elapsed / NANOS_PER_SECOND * refillPerSecond);
        lastRefillNanos = now;
        if (tokens < permits) {
            return false;
        }
        tokens -= permits;
        return true;
        // --->8--- solution
    }
}
