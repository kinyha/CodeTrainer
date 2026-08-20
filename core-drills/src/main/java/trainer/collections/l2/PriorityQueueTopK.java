package trainer.collections.l2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

// @task collections.l2.PriorityQueueTopK
// @tags PriorityQueue,heap,top-k
// @time 15m
// @src  new
public final class PriorityQueueTopK {

    private PriorityQueueTopK() {
    }

    /**
     * k наибольших значений через min-heap размера k: если новый элемент больше вершины кучи,
     * вершина вытесняется. Результат отсортирован по возрастанию.
     */
    public static List<Integer> topK(List<Integer> values, int k) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int value : values) {
            heap.add(value);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        List<Integer> result = new ArrayList<>(heap);
        Collections.sort(result);
        return result;
        // --->8--- solution
    }
}
