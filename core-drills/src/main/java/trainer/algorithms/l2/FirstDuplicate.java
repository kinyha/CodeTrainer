package trainer.algorithms.l2;

import java.util.HashSet;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;

// @task algorithms.l2.FirstDuplicate
// @tags arrays,hash-set,order
// @time 10m
// @src  new
public final class FirstDuplicate {

    private FirstDuplicate() {
    }

    /** Возвращает значение, чьё ВТОРОЕ вхождение встречается раньше всех остальных повторов. */
    public static OptionalInt find(int[] values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        Set<Integer> seen = new HashSet<>();
        for (int value : values) {
            if (!seen.add(value)) {
                return OptionalInt.of(value);
            }
        }
        return OptionalInt.empty();
        // --->8--- solution
    }
}
