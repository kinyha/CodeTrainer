package trainer.concurrency.l2;

import java.util.concurrent.atomic.AtomicLong;

// @task concurrency.l2.AtomicCounter
// @tags concurrency,AtomicLong,lock-free,counter
// @time 12m
// @src  new
public final class AtomicCounter {

    private final AtomicLong value = new AtomicLong();

    /** Атомарно увеличивает счётчик и возвращает новое значение. */
    public long incrementAndGet() {
        // ---8<--- solution
        return value.incrementAndGet();
        // --->8--- solution
    }

    public long get() {
        return value.get();
    }
}
