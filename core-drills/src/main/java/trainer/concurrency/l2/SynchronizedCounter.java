package trainer.concurrency.l2;

// @task concurrency.l2.SynchronizedCounter
// @tags concurrency,synchronized,mutual-exclusion,counter
// @time 10m
// @src  new
public final class SynchronizedCounter {

    private long value;

    /** synchronized на мониторе экземпляра сериализует доступ — без него ++ не атомарен. */
    public synchronized long incrementAndGet() {
        // ---8<--- solution
        value++;
        return value;
        // --->8--- solution
    }

    public synchronized long get() {
        return value;
    }
}
