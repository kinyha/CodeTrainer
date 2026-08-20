package trainer.algorithms.l1;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// @task algorithms.l1.ContainsDuplicate
// @tags arrays,hash-set
// @time 6m
// @src  new
public final class ContainsDuplicate {

    private ContainsDuplicate() {
    }

    /** true, если хотя бы одно значение встречается больше одного раза. */
    public static boolean test(int[] values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        Set<Integer> seen = new HashSet<>();
        for (int value : values) {
            if (!seen.add(value)) {
                return true;
            }
        }
        return false;
        // --->8--- solution
    }
}
