package trainer.concurrency.l3;

import java.util.concurrent.atomic.AtomicInteger;

// @task concurrency.l3.AtomicCasIncrement
// @tags concurrency,compareAndSet,CAS,retry-loop
// @time 25m
// @src  new
public final class AtomicCasIncrement {

    private final AtomicInteger value = new AtomicInteger();

    /**
     * Вручную то, что incrementAndGet() делает внутри: читаем, вычисляем, пытаемся
     * compareAndSet — если между чтением и записью значение уехало, повторяем.
     */
    public int incrementIfBelow(int ceiling) {
        // ---8<--- solution
        while (true) {
            int current = value.get();
            if (current >= ceiling) {
                return current;
            }
            int next = current + 1;
            if (value.compareAndSet(current, next)) {
                return next;
            }
        }
        // --->8--- solution
    }

    public int get() {
        return value.get();
    }
}
