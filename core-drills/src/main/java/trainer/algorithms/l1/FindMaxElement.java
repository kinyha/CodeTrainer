package trainer.algorithms.l1;

import java.util.Objects;
import java.util.OptionalInt;

// @task algorithms.l1.FindMaxElement
// @tags arrays,optional,loop
// @time 6m
// @src  new
public final class FindMaxElement {

    private FindMaxElement() {
    }

    /** Пустой массив даёт OptionalInt.empty(), а не исключение. */
    public static OptionalInt max(int[] values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        if (values.length == 0) {
            return OptionalInt.empty();
        }
        int max = values[0];
        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }
        return OptionalInt.of(max);
        // --->8--- solution
    }
}
