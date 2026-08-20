package trainer.concurrency.l2;

import java.util.Objects;

// @task concurrency.l2.VolatileStopFlag
// @tags concurrency,volatile,visibility,stop-flag
// @time 10m
// @src  new
public final class VolatileStopFlag {

    private volatile boolean stopped = false;

    public void stop() {
        stopped = true;
    }

    /** volatile гарантирует, что stop() из другого потока сразу виден здесь — иначе цикл может не заметить флаг. */
    public void run(Runnable tick) {
        Objects.requireNonNull(tick, "tick");

        // ---8<--- solution
        while (!stopped) {
            tick.run();
        }
        // --->8--- solution
    }
}
