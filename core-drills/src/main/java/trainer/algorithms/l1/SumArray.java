package trainer.algorithms.l1;

import java.util.Objects;

// @task algorithms.l1.SumArray
// @tags arrays,loop,overflow
// @time 5m
// @src  new
public final class SumArray {

    private SumArray() {
    }

    /** Суммирует элементы в long, чтобы не переполниться на больших int-значениях. */
    public static long sum(int[] values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        long total = 0;
        for (int value : values) {
            total += value;
        }
        return total;
        // --->8--- solution
    }
}
