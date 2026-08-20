package trainer.concurrency.l2;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ExecutorSubmitAndShutdownTest {

    @Test
    void runsAllTasksAndTerminatesTheExecutor() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Future<Integer>> futures = ExecutorSubmitAndShutdown.runAll(executor, List.of(() -> 1, () -> 2, () -> 3));

        int sum = 0;
        for (Future<Integer> future : futures) {
            sum += future.get();
        }
        assertThat(sum).isEqualTo(6);
        assertThat(executor.isTerminated()).isTrue();
    }

    @Test
    void rejectsNull() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        assertThatNullPointerException().isThrownBy(() -> ExecutorSubmitAndShutdown.runAll(null, List.of()));
        assertThatNullPointerException().isThrownBy(() -> ExecutorSubmitAndShutdown.runAll(executor, null));
        executor.shutdownNow();
    }
}
