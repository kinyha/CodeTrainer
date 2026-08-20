package trainer.concurrency.l1;

import java.util.Objects;

// @task concurrency.l1.DaemonThreadFlag
// @tags concurrency,Thread,daemon
// @time 5m
// @src  new
public final class DaemonThreadFlag {

    private DaemonThreadFlag() {
    }

    /** daemon-поток не удерживает JVM от завершения; ставить нужно ДО start(). */
    public static Thread newDaemon(Runnable task) {
        Objects.requireNonNull(task, "task");

        // ---8<--- solution
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        return thread;
        // --->8--- solution
    }
}
