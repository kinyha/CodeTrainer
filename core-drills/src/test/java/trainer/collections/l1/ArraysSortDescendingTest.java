package trainer.collections.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ArraysSortDescendingTest {

    @Test
    void sortsFromLargestToSmallest() {
        Integer[] values = {3, 1, 4, 1, 5, 9, 2};
        ArraysSortDescending.sortDescending(values);
        assertThat(values).containsExactly(9, 5, 4, 3, 2, 1, 1);
    }

    @Test
    void singleElementArrayStaysTheSame() {
        Integer[] values = {7};
        ArraysSortDescending.sortDescending(values);
        assertThat(values).containsExactly(7);
    }

    @Test
    void emptyArrayStaysEmpty() {
        Integer[] values = {};
        ArraysSortDescending.sortDescending(values);
        assertThat(values).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ArraysSortDescending.sortDescending(null));
    }
}
