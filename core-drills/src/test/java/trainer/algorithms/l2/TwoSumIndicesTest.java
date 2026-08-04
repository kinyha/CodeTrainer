package trainer.algorithms.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TwoSumIndicesTest {

    @Test
    void findsPairWithoutReusingElement() {
        assertThat(TwoSumIndices.find(new int[]{3, 3}, 6))
                .contains(new TwoSumIndices.Indices(0, 1));
    }

    @Test
    void keepsFirstIndexForDuplicateValues() {
        assertThat(TwoSumIndices.find(new int[]{1, 1, 4, 5}, 6))
                .contains(new TwoSumIndices.Indices(0, 3));
    }

    @Test
    void returnsEmptyWhenPairDoesNotExist() {
        assertThat(TwoSumIndices.find(new int[]{1, 2}, 10)).isEmpty();
    }
}
