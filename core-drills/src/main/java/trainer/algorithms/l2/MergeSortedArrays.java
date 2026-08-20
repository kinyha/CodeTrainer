package trainer.algorithms.l2;

import java.util.Objects;

// @task algorithms.l2.MergeSortedArrays
// @tags arrays,two-pointers,merge
// @time 12m
// @src  new
public final class MergeSortedArrays {

    private MergeSortedArrays() {
    }

    /** Оба массива должны быть отсортированы по возрастанию. */
    public static int[] merge(int[] first, int[] second) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");

        // ---8<--- solution
        int[] result = new int[first.length + second.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < first.length && j < second.length) {
            result[k++] = first[i] <= second[j] ? first[i++] : second[j++];
        }
        while (i < first.length) {
            result[k++] = first[i++];
        }
        while (j < second.length) {
            result[k++] = second[j++];
        }
        return result;
        // --->8--- solution
    }
}
