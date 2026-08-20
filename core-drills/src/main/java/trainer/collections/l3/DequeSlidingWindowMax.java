package trainer.collections.l3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

// @task collections.l3.DequeSlidingWindowMax
// @tags Deque,sliding-window,monotonic
// @time 28m
// @src  new
public final class DequeSlidingWindowMax {

    private DequeSlidingWindowMax() {
    }

    /**
     * Максимум в каждом окне размера windowSize. Deque хранит индексы по убыванию значений:
     * перед вставкой убираем сзади всё, что меньше нового элемента — оно уже никогда не станет
     * максимумом, пока жив новый.
     */
    public static List<Integer> maxPerWindow(int[] values, int windowSize) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        if (windowSize <= 0 || windowSize > values.length) {
            throw new IllegalArgumentException("windowSize must be between 1 and values.length");
        }
        Deque<Integer> indices = new ArrayDeque<>(); // индексы, значения убывают от головы к хвосту
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            while (!indices.isEmpty() && values[indices.peekLast()] <= values[i]) {
                indices.pollLast();
            }
            indices.addLast(i);
            if (indices.peekFirst() <= i - windowSize) {
                indices.pollFirst();
            }
            if (i >= windowSize - 1) {
                result.add(values[indices.peekFirst()]);
            }
        }
        return result;
        // --->8--- solution
    }
}
