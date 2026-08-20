package trainer.patterns.l2;

import java.util.List;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

// @task patterns.l2.DecoratorLoggingWrapper
// @tags patterns,decorator,cross-cutting-concern
// @time 15m
// @src  new
public final class DecoratorLoggingWrapper {

    private DecoratorLoggingWrapper() {
    }

    /** Декоратор оборачивает поведение, не трогая исходную реализацию и не меняя её тип. */
    public static IntUnaryOperator withLogging(IntUnaryOperator delegate, List<String> log) {
        Objects.requireNonNull(delegate, "delegate");
        Objects.requireNonNull(log, "log");

        // ---8<--- solution
        return input -> {
            int result = delegate.applyAsInt(input);
            log.add(input + " -> " + result);
            return result;
        };
        // --->8--- solution
    }
}
