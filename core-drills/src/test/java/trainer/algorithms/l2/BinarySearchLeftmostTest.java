package trainer.algorithms.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BinarySearchLeftmostTest {

    @Test
    void findsLeftmostIndexAmongDuplicates() {
        assertThat(BinarySearchLeftmost.indexOf(new int[]{1, 2, 2, 2, 3, 5}, 2)).isEqualTo(1);
    }

    @Test
    void findsUniqueValue() {
        assertThat(BinarySearchLeftmost.indexOf(new int[]{1, 2, 2, 2, 3, 5}, 5)).isEqualTo(5);
    }

    @Test
    void missingValueReturnsMinusOne() {
        assertThat(BinarySearchLeftmost.indexOf(new int[]{1, 2, 2, 2, 3, 5}, 4)).isEqualTo(-1);
    }

    @Test
    void emptyArrayReturnsMinusOne() {
        assertThat(BinarySearchLeftmost.indexOf(new int[0], 1)).isEqualTo(-1);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> BinarySearchLeftmost.indexOf(null, 1));
    }
}
