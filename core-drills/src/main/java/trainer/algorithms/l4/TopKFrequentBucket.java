package trainer.algorithms.l4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @task algorithms.l4.TopKFrequentBucket
// @tags arrays,bucket-sort,frequency,linear-time
// @time 45m
// @src  new
public final class TopKFrequentBucket {

    private TopKFrequentBucket() {
    }

    /**
     * Bucket sort по частоте: индекс корзины — сама частота, поэтому сортировка занимает
     * O(n) вместо O(n log n) у сортировки по компаратору или кучи. При равной частоте
     * порядок между значениями не гарантирован.
     */
    public static List<Integer> topK(int[] values, int k) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        Map<Integer, Integer> frequencyByValue = new HashMap<>();
        for (int value : values) {
            frequencyByValue.merge(value, 1, Integer::sum);
        }

        List<List<Integer>> bucketsByFrequency = new ArrayList<>(values.length + 1);
        for (int i = 0; i <= values.length; i++) {
            bucketsByFrequency.add(new ArrayList<>());
        }
        for (Map.Entry<Integer, Integer> entry : frequencyByValue.entrySet()) {
            bucketsByFrequency.get(entry.getValue()).add(entry.getKey());
        }

        List<Integer> result = new ArrayList<>(k);
        for (int frequency = bucketsByFrequency.size() - 1; frequency >= 0 && result.size() < k; frequency--) {
            for (int value : bucketsByFrequency.get(frequency)) {
                if (result.size() == k) {
                    break;
                }
                result.add(value);
            }
        }
        return result;
        // --->8--- solution
    }
}
