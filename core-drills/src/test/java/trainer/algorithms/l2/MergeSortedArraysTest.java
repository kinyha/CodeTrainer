package trainer.algorithms.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MergeSortedArraysTest {

    @Test
    void interleavesTwoSortedArrays() {
        assertThat(MergeSortedArrays.merge(new int[]{1, 3, 5}, new int[]{2, 4, 6}))
                .containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    void keepsDuplicatesFromBothArrays() {
        assertThat(MergeSortedArrays.merge(new int[]{1, 2}, new int[]{2, 3}))
                .containsExactly(1, 2, 2, 3);
    }

    @Test
    void handlesOneEmptyArray() {
        assertThat(MergeSortedArrays.merge(new int[0], new int[]{1, 2})).containsExactly(1, 2);
        assertThat(MergeSortedArrays.merge(new int[]{1, 2}, new int[0])).containsExactly(1, 2);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> MergeSortedArrays.merge(null, new int[0]));
        assertThatNullPointerException().isThrownBy(() -> MergeSortedArrays.merge(new int[0], null));
    }
}
