package trainer.concurrency.l1;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class RunnableVsCallableTest {

    @Test
    void runsSuccessfullyWithoutInvokingOnError() {
        AtomicReference<Throwable> captured = new AtomicReference<>();
        AtomicReference<String> ran = new AtomicReference<>();

        Runnable runnable = RunnableVsCallable.toRunnable(() -> {
            ran.set("done");
            return null;
        }, captured::set);
        runnable.run();

        assertThat(ran.get()).isEqualTo("done");
        assertThat(captured.get()).isNull();
    }

    @Test
    void forwardsCheckedExceptionToOnError() {
        AtomicReference<Throwable> captured = new AtomicReference<>();
        Exception checked = new Exception("boom");

        Runnable runnable = RunnableVsCallable.toRunnable(() -> {
            throw checked;
        }, captured::set);
        runnable.run();

        assertThat(captured.get()).isSameAs(checked);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> RunnableVsCallable.toRunnable(null, error -> {}));
        assertThatNullPointerException().isThrownBy(() -> RunnableVsCallable.toRunnable(() -> null, null));
    }
}
