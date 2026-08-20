package trainer.algorithms.l2;

import java.util.Objects;

// @task algorithms.l2.RemoveDuplicatesSorted
// @tags arrays,two-pointers,in-place
// @time 12m
// @src  new
public final class RemoveDuplicatesSorted {

    private RemoveDuplicatesSorted() {
    }

    /**
     * Массив должен быть отсортирован по возрастанию. Уникальные значения переносятся
     * в начало массива на месте; возвращает их количество. Хвост массива после этой
     * границы не определён.
     */
    public static int deduplicate(int[] sorted) {
        Objects.requireNonNull(sorted, "sorted");

        // ---8<--- solution
        if (sorted.length == 0) {
            return 0;
        }
        int write = 1;
        for (int read = 1; read < sorted.length; read++) {
            if (sorted[read] != sorted[write - 1]) {
                sorted[write++] = sorted[read];
            }
        }
        return write;
        // --->8--- solution
    }
}
