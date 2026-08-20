package trainer.algorithms.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ReverseStringInPlaceTest {

    @Test
    void reversesEvenLengthArray() {
        char[] value = "abcd".toCharArray();
        ReverseStringInPlace.reverse(value);
        assertThat(value).containsExactly('d', 'c', 'b', 'a');
    }

    @Test
    void reversesOddLengthArray() {
        char[] value = "abcde".toCharArray();
        ReverseStringInPlace.reverse(value);
        assertThat(value).containsExactly('e', 'd', 'c', 'b', 'a');
    }

    @Test
    void leavesEmptyAndSingleCharArraysUnchanged() {
        char[] empty = new char[0];
        ReverseStringInPlace.reverse(empty);
        assertThat(empty).isEmpty();

        char[] single = {'x'};
        ReverseStringInPlace.reverse(single);
        assertThat(single).containsExactly('x');
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ReverseStringInPlace.reverse(null));
    }
}
