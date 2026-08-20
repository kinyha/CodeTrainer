package trainer.concurrency.l1;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DaemonThreadFlagTest {

    @Test
    void createsAThreadMarkedAsDaemon() {
        Thread thread = DaemonThreadFlag.newDaemon(() -> {});
        assertThat(thread.isDaemon()).isTrue();
        assertThat(thread.getState()).isEqualTo(Thread.State.NEW);
    }

    @Test
    void runsTheGivenTaskWhenStarted() throws InterruptedException {
        AtomicBoolean ran = new AtomicBoolean();
        Thread thread = DaemonThreadFlag.newDaemon(() -> ran.set(true));

        thread.start();
        thread.join();

        assertThat(ran.get()).isTrue();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> DaemonThreadFlag.newDaemon(null));
    }
}
