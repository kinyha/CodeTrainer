package trainer.patterns.l3;

import java.util.Objects;
import java.util.concurrent.Callable;

// @task patterns.l3.DecoratorRetry
// @tags patterns,decorator,retry,exponential-backoff
// @time 28m
// @src  new
public final class DecoratorRetry {

    private DecoratorRetry() {
    }

    /** Декоратор добавляет retry поверх любого Callable, не меняя его контракт вызова. */
    public static <T> Callable<T> withRetry(Callable<T> delegate, int maxAttempts) {
        Objects.requireNonNull(delegate, "delegate");

        // ---8<--- solution
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be positive");
        }
        return () -> {
            Exception lastError = null;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    return delegate.call();
                } catch (Exception error) {
                    lastError = error;
                }
            }
            throw lastError;
        };
        // --->8--- solution
    }
}
