package trainer.algorithms.l2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

// @task algorithms.l2.TwoSumIndices
// @tags arrays,hash-map,two-sum
// @time 15m
// @src  new
public final class TwoSumIndices {

    private TwoSumIndices() {
    }

    /** Возвращает первую пару в порядке просмотра правого индекса. */
    public static Optional<Indices> find(int[] values, int target) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        Map<Integer, Integer> firstIndex = new HashMap<>();
        for (int right = 0; right < values.length; right++) {
            int complement = target - values[right];
            Integer left = firstIndex.get(complement);
            if (left != null) {
                return Optional.of(new Indices(left, right));
            }
            firstIndex.putIfAbsent(values[right], right);
        }
        return Optional.empty();
        // --->8--- solution
    }

    public record Indices(int left, int right) {
        public Indices {
            if (left < 0 || right <= left) {
                throw new IllegalArgumentException("indices must be ordered");
            }
        }
    }
}
