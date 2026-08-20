package trainer.algorithms.l2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class RemoveDuplicatesSortedTest {

    @Test
    void movesUniqueValuesToTheFront() {
        int[] values = {1, 1, 2, 2, 2, 3};
        int length = RemoveDuplicatesSorted.deduplicate(values);

        assertThat(length).isEqualTo(3);
        assertThat(Arrays.copyOf(values, length)).containsExactly(1, 2, 3);
    }

    @Test
    void arrayWithoutDuplicatesKeepsFullLength() {
        int[] values = {1, 2, 3};
        assertThat(RemoveDuplicatesSorted.deduplicate(values)).isEqualTo(3);
    }

    @Test
    void allSameValueCollapsesToOne() {
        int[] values = {4, 4, 4};
        assertThat(RemoveDuplicatesSorted.deduplicate(values)).isEqualTo(1);
    }

    @Test
    void emptyArrayGivesZero() {
        assertThat(RemoveDuplicatesSorted.deduplicate(new int[0])).isEqualTo(0);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> RemoveDuplicatesSorted.deduplicate(null));
    }
}
