package trainer.concurrency.l1;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

// @task concurrency.l1.RunnableVsCallable
// @tags concurrency,Runnable,Callable,checked-exceptions
// @time 8m
// @src  new
public final class RunnableVsCallable {

    private RunnableVsCallable() {
    }

    /** Runnable.run() не может бросать checked-исключения — Callable может. Оборачиваем и ловим. */
    public static Runnable toRunnable(Callable<?> callable, Consumer<Throwable> onError) {
        Objects.requireNonNull(callable, "callable");
        Objects.requireNonNull(onError, "onError");

        // ---8<--- solution
        return () -> {
            try {
                callable.call();
            } catch (Exception error) {
                onError.accept(error);
            }
        };
        // --->8--- solution
    }
}
