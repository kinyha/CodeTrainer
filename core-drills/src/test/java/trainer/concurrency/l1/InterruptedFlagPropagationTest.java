package trainer.concurrency.l1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class InterruptedFlagPropagationTest {

    @AfterEach
    void clearInterruptFlag() {
        Thread.interrupted(); // сбрасывает флаг, чтобы не протекал в другие тесты
    }

    @Test
    void returnsTaskResultWhenNotInterrupted() {
        Object result = InterruptedFlagPropagation.runSwallowingInterrupt(() -> "done", "fallback");
        assertThat(result).isEqualTo("done");
        assertThat(Thread.currentThread().isInterrupted()).isFalse();
    }

    @Test
    void restoresInterruptFlagAndReturnsFallback() {
        Object result = InterruptedFlagPropagation.runSwallowingInterrupt(() -> {
            throw new InterruptedException();
        }, "fallback");

        assertThat(result).isEqualTo("fallback");
        assertThat(Thread.currentThread().isInterrupted()).isTrue();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> InterruptedFlagPropagation.runSwallowingInterrupt(null, "x"));
    }
}
