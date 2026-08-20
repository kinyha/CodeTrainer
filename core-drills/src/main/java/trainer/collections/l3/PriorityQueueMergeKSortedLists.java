package trainer.collections.l3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

// @task collections.l3.PriorityQueueMergeKSortedLists
// @tags PriorityQueue,heap,merge,k-way
// @time 25m
// @src  new
public final class PriorityQueueMergeKSortedLists {

    private PriorityQueueMergeKSortedLists() {
    }

    /** Каждый вложенный список должен быть отсортирован по возрастанию. */
    public static List<Integer> mergeAll(List<List<Integer>> sortedLists) {
        Objects.requireNonNull(sortedLists, "sortedLists");

        // ---8<--- solution
        PriorityQueue<Cursor> heap = new PriorityQueue<>(Comparator.comparingInt(Cursor::value));
        for (int listIndex = 0; listIndex < sortedLists.size(); listIndex++) {
            List<Integer> list = sortedLists.get(listIndex);
            if (!list.isEmpty()) {
                heap.add(new Cursor(listIndex, 0, list.get(0)));
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!heap.isEmpty()) {
            Cursor cursor = heap.poll();
            result.add(cursor.value());
            List<Integer> list = sortedLists.get(cursor.listIndex());
            int nextElementIndex = cursor.elementIndex() + 1;
            if (nextElementIndex < list.size()) {
                heap.add(new Cursor(cursor.listIndex(), nextElementIndex, list.get(nextElementIndex)));
            }
        }
        return result;
        // --->8--- solution
    }

    private record Cursor(int listIndex, int elementIndex, int value) {
    }
}
