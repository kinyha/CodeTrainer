package trainer.concurrency.l3;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

// @task concurrency.l3.CountDownLatchCoordination
// @tags concurrency,CountDownLatch,fan-out,fan-in
// @time 25m
// @src  new
public final class CountDownLatchCoordination {

    private CountDownLatchCoordination() {
    }

    /**
     * Fan-out: запускает workerCount воркеров и ждёт, пока ВСЕ завершатся, прежде чем
     * вернуть итоговую сумму. await() — единственная точка синхронизации между ними и вызывающим.
     */
    public static int runAllAndSum(ExecutorService executor, int workerCount) throws InterruptedException {
        Objects.requireNonNull(executor, "executor");

        // ---8<--- solution
        if (workerCount <= 0) {
            throw new IllegalArgumentException("workerCount must be positive");
        }
        CountDownLatch done = new CountDownLatch(workerCount);
        AtomicInteger sum = new AtomicInteger();
        for (int i = 0; i < workerCount; i++) {
            int workerId = i;
            executor.submit(() -> {
                sum.addAndGet(workerId + 1);
                done.countDown();
            });
        }
        done.await();
        return sum.get();
        // --->8--- solution
    }
}
