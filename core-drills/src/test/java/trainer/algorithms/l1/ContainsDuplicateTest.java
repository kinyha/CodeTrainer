package trainer.algorithms.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ContainsDuplicateTest {

    @Test
    void detectsRepeatedValue() {
        assertThat(ContainsDuplicate.test(new int[]{1, 2, 3, 2})).isTrue();
    }

    @Test
    void allUniqueValuesReturnFalse() {
        assertThat(ContainsDuplicate.test(new int[]{1, 2, 3})).isFalse();
    }

    @Test
    void emptyAndSingleElementArraysHaveNoDuplicates() {
        assertThat(ContainsDuplicate.test(new int[0])).isFalse();
        assertThat(ContainsDuplicate.test(new int[]{7})).isFalse();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ContainsDuplicate.test(null));
    }
}
