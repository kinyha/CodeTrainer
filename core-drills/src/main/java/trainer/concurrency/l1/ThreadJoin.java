package trainer.concurrency.l1;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

// @task concurrency.l1.ThreadJoin
// @tags concurrency,Thread,join
// @time 8m
// @src  new
public final class ThreadJoin {

    private ThreadJoin() {
    }

    /** join() гарантирует happens-before: без него значение из другого потока не видно надёжно. */
    public static <T> T runAndAwait(Supplier<T> task) throws InterruptedException {
        Objects.requireNonNull(task, "task");

        // ---8<--- solution
        AtomicReference<T> result = new AtomicReference<>();
        Thread worker = new Thread(() -> result.set(task.get()));
        worker.start();
        worker.join();
        return result.get();
        // --->8--- solution
    }
}
