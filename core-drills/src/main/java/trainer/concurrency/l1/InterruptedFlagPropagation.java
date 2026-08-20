package trainer.concurrency.l1;

import java.util.Objects;
import java.util.concurrent.Callable;

// @task concurrency.l1.InterruptedFlagPropagation
// @tags concurrency,InterruptedException,interrupt-flag
// @time 8m
// @src  new
public final class InterruptedFlagPropagation {

    private InterruptedFlagPropagation() {
    }

    /**
     * Проглотить InterruptedException нельзя: если не можешь бросить его дальше,
     * обязан восстановить флаг через Thread.currentThread().interrupt().
     */
    public static <T> T runSwallowingInterrupt(Callable<T> task, T fallback) {
        Objects.requireNonNull(task, "task");

        // ---8<--- solution
        try {
            return task.call();
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            return fallback;
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
        // --->8--- solution
    }
}
