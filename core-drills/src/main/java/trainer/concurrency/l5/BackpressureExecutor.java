package trainer.concurrency.l5;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

// @task concurrency.l5.BackpressureExecutor
// @tags concurrency,executor,semaphore,backpressure,rejection
// @time 65m
// @src  new
// @doc  BackpressureExecutor.md
public final class BackpressureExecutor {

    private final Executor delegate;
    private final Semaphore slots;

    public BackpressureExecutor(Executor delegate, int maxInFlight) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        if (maxInFlight <= 0) throw new IllegalArgumentException("maxInFlight must be positive");
        this.slots = new Semaphore(maxInFlight, true);
    }

    /** Блокирует submitter при перегрузке и освобождает permit при любом завершении. */
    public void submit(Runnable task) throws InterruptedException {
        Objects.requireNonNull(task, "task");

        // ---8<--- solution
        slots.acquire();
        boolean accepted = false;
        try {
            delegate.execute(() -> {
                try {
                    task.run();
                } finally {
                    slots.release();
                }
            });
            accepted = true;
        } finally {
            if (!accepted) {
                slots.release(); // EDGE: rejected delegate не должен навсегда съесть capacity
            }
        }
        // --->8--- solution
    }

    int availableSlots() {
        return slots.availablePermits();
    }
}
