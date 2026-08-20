package trainer.algorithms.l2;

import java.util.Objects;

// @task algorithms.l2.MaxSubarraySum
// @tags arrays,kadane,dynamic-programming
// @time 15m
// @src  new
public final class MaxSubarraySum {

    private MaxSubarraySum() {
    }

    /** Алгоритм Кадане. Массив не может быть пустым — максимума не существует. */
    public static long maxSum(int[] values) {
        Objects.requireNonNull(values, "values");
        if (values.length == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }

        // ---8<--- solution
        long best = values[0];
        long current = values[0];
        for (int i = 1; i < values.length; i++) {
            current = Math.max(values[i], current + values[i]);
            best = Math.max(best, current);
        }
        return best;
        // --->8--- solution
    }
}
