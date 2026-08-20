package trainer.algorithms.l2;

import java.util.Objects;

// @task algorithms.l2.BinarySearchLeftmost
// @tags arrays,binary-search,duplicates
// @time 15m
// @src  new
public final class BinarySearchLeftmost {

    private BinarySearchLeftmost() {
    }

    /** Массив должен быть отсортирован по возрастанию. Возвращает -1, если target не найден. */
    public static int indexOf(int[] sorted, int target) {
        Objects.requireNonNull(sorted, "sorted");

        // ---8<--- solution
        int low = 0;
        int high = sorted.length - 1;
        int result = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (sorted[mid] < target) {
                low = mid + 1;
            } else if (sorted[mid] > target) {
                high = mid - 1;
            } else {
                result = mid;
                high = mid - 1;
            }
        }
        return result;
        // --->8--- solution
    }
}
