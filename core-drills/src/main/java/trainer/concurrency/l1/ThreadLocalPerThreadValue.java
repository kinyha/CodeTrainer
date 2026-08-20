package trainer.concurrency.l1;

// @task concurrency.l1.ThreadLocalPerThreadValue
// @tags concurrency,ThreadLocal,isolation
// @time 8m
// @src  new
public final class ThreadLocalPerThreadValue {

    private static final ThreadLocal<Integer> COUNTER = ThreadLocal.withInitial(() -> 0);

    private ThreadLocalPerThreadValue() {
    }

    /** Каждый поток видит свой независимый счётчик — increment() в одном не влияет на другой. */
    public static int incrementAndGet() {
        // ---8<--- solution
        int next = COUNTER.get() + 1;
        COUNTER.set(next);
        return next;
        // --->8--- solution
    }
}
