package trainer.collections.l1;

import java.util.List;
import java.util.Objects;

// @task collections.l1.DefensiveListCopy
// @tags List,copyOf,immutability,defensive-copy
// @time 5m
// @src  new
public final class DefensiveListCopy {

    private DefensiveListCopy() {
    }

    /** Неизменяемая копия: правки исходного списка после вызова наружу не видны. */
    public static <T> List<T> copy(List<T> values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        return List.copyOf(values);
        // --->8--- solution
    }
}
