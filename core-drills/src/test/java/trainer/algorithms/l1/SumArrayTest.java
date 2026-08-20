package trainer.algorithms.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class SumArrayTest {

    @Test
    void sumsPositiveAndNegativeValues() {
        assertThat(SumArray.sum(new int[]{1, -2, 3, 4})).isEqualTo(6);
    }

    @Test
    void emptyArraySumsToZero() {
        assertThat(SumArray.sum(new int[0])).isZero();
    }

    @Test
    void doesNotOverflowOnLargeIntValues() {
        int[] values = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        assertThat(SumArray.sum(values)).isEqualTo(2L * Integer.MAX_VALUE);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> SumArray.sum(null));
    }
}
