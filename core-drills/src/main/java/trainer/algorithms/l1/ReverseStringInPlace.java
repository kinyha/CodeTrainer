package trainer.algorithms.l1;

import java.util.Objects;

// @task algorithms.l1.ReverseStringInPlace
// @tags strings,two-pointers,in-place
// @time 6m
// @src  new
public final class ReverseStringInPlace {

    private ReverseStringInPlace() {
    }

    /** Разворачивает массив символов на месте, не выделяя новый массив. */
    public static void reverse(char[] value) {
        Objects.requireNonNull(value, "value");

        // ---8<--- solution
        int left = 0;
        int right = value.length - 1;
        while (left < right) {
            char temp = value[left];
            value[left] = value[right];
            value[right] = temp;
            left++;
            right--;
        }
        // --->8--- solution
    }
}
