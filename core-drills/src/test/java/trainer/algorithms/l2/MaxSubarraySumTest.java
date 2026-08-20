package trainer.algorithms.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MaxSubarraySumTest {

    @Test
    void findsBestContiguousRunAcrossSignChanges() {
        assertThat(MaxSubarraySum.maxSum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4})).isEqualTo(6);
    }

    @Test
    void allNegativeValuesGiveTheLeastNegativeOne() {
        assertThat(MaxSubarraySum.maxSum(new int[]{-3, -1, -2})).isEqualTo(-1);
    }

    @Test
    void singleElementIsItsOwnSum() {
        assertThat(MaxSubarraySum.maxSum(new int[]{7})).isEqualTo(7);
    }

    @Test
    void rejectsEmptyArray() {
        assertThatIllegalArgumentException().isThrownBy(() -> MaxSubarraySum.maxSum(new int[0]));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> MaxSubarraySum.maxSum(null));
    }
}
