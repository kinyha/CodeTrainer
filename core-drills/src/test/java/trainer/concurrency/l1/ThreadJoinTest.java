package trainer.concurrency.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ThreadJoinTest {

    @Test
    void returnsTheValueComputedByTheWorkerThread() throws InterruptedException {
        assertThat(ThreadJoin.runAndAwait(() -> 21 * 2)).isEqualTo(42);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ThreadJoin.runAndAwait(null));
    }
}
