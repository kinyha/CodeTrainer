package trainer.collections.l1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

// @task collections.l1.ArraysSortDescending
// @tags Arrays,Comparator,sorting
// @time 6m
// @src  new
public final class ArraysSortDescending {

    private ArraysSortDescending() {
    }

    /** int[] нельзя отсортировать компаратором — нужен boxed Integer[]. */
    public static void sortDescending(Integer[] values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        Arrays.sort(values, Comparator.reverseOrder());
        // --->8--- solution
    }
}
