package trainer.patterns.l3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DecoratorRetryTest {

    @Test
    void returnsResultWithoutRetryingOnFirstSuccess() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        Callable<String> delegate = () -> {
            calls.incrementAndGet();
            return "ok";
        };

        assertThat(DecoratorRetry.withRetry(delegate, 3).call()).isEqualTo("ok");
        assertThat(calls.get()).isEqualTo(1);
    }

    @Test
    void retriesUntilSuccessWithinMaxAttempts() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        Callable<String> delegate = () -> {
            if (calls.incrementAndGet() < 3) {
                throw new RuntimeException("transient");
            }
            return "ok";
        };

        assertThat(DecoratorRetry.withRetry(delegate, 5).call()).isEqualTo("ok");
        assertThat(calls.get()).isEqualTo(3);
    }

    @Test
    void rethrowsLastErrorWhenAttemptsAreExhausted() {
        Callable<String> delegate = () -> {
            throw new RuntimeException("always fails");
        };

        assertThatThrownBy(() -> DecoratorRetry.withRetry(delegate, 2).call())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("always fails");
    }

    @Test
    void rejectsNonPositiveMaxAttempts() {
        assertThatIllegalArgumentException().isThrownBy(() -> DecoratorRetry.withRetry(() -> "x", 0));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> DecoratorRetry.withRetry(null, 3));
    }
}
