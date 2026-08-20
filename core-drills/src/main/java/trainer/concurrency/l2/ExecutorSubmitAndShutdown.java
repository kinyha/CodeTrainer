package trainer.concurrency.l2;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// @task concurrency.l2.ExecutorSubmitAndShutdown
// @tags concurrency,ExecutorService,shutdown,awaitTermination
// @time 12m
// @src  new
public final class ExecutorSubmitAndShutdown {

    private ExecutorSubmitAndShutdown() {
    }

    /**
     * Graceful shutdown: сначала submit всех задач, потом shutdown() (новые задачи больше
     * не принимаются) и awaitTermination — дожидаемся уже принятых, а не обрываем их.
     */
    public static <T> List<Future<T>> runAll(ExecutorService executor, List<Callable<T>> tasks) throws InterruptedException {
        Objects.requireNonNull(executor, "executor");
        Objects.requireNonNull(tasks, "tasks");

        // ---8<--- solution
        List<Future<T>> futures = tasks.stream().map(executor::submit).toList();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        return futures;
        // --->8--- solution
    }
}
