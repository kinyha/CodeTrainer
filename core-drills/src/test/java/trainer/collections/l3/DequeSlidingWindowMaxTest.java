package trainer.collections.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DequeSlidingWindowMaxTest {

    @Test
    void slidesAWindowOfThreeAcrossTheArray() {
        int[] values = {1, 3, -1, -3, 5, 3, 6, 7};
        assertThat(DequeSlidingWindowMax.maxPerWindow(values, 3)).containsExactly(3, 3, 5, 5, 6, 7);
    }

    @Test
    void windowOfOneReturnsTheArrayItself() {
        assertThat(DequeSlidingWindowMax.maxPerWindow(new int[]{4, 1, 2}, 1)).containsExactly(4, 1, 2);
    }

    @Test
    void windowSpanningTheWholeArrayGivesOneValue() {
        assertThat(DequeSlidingWindowMax.maxPerWindow(new int[]{4, 1, 9, 2}, 4)).containsExactly(9);
    }

    @Test
    void rejectsWindowSizeOutOfRange() {
        assertThatIllegalArgumentException().isThrownBy(() -> DequeSlidingWindowMax.maxPerWindow(new int[]{1, 2}, 0));
        assertThatIllegalArgumentException().isThrownBy(() -> DequeSlidingWindowMax.maxPerWindow(new int[]{1, 2}, 3));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> DequeSlidingWindowMax.maxPerWindow(null, 1));
    }
}
