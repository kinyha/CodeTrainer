package trainer.concurrency.l3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CountDownLatchCoordinationTest {

    @Test
    void waitsForAllWorkersBeforeReturningTheSum() throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            assertThat(CountDownLatchCoordination.runAllAndSum(executor, 5)).isEqualTo(15);
        }
    }

    @Test
    void rejectsNonPositiveWorkerCount() throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            assertThatIllegalArgumentException().isThrownBy(() -> CountDownLatchCoordination.runAllAndSum(executor, 0));
        }
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CountDownLatchCoordination.runAllAndSum(null, 1));
    }
}
