package trainer.collections.l3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriorityQueueMergeKSortedListsTest {

    @Test
    void mergesThreeSortedListsIntoOne() {
        List<List<Integer>> lists = List.of(List.of(1, 4, 7), List.of(2, 5), List.of(3, 6, 8, 9));
        assertThat(PriorityQueueMergeKSortedLists.mergeAll(lists)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    void skipsEmptyListsInTheInput() {
        List<List<Integer>> lists = List.of(List.of(), List.of(1, 2), List.of());
        assertThat(PriorityQueueMergeKSortedLists.mergeAll(lists)).containsExactly(1, 2);
    }

    @Test
    void noListsGiveEmptyResult() {
        assertThat(PriorityQueueMergeKSortedLists.mergeAll(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> PriorityQueueMergeKSortedLists.mergeAll(null));
    }
}
