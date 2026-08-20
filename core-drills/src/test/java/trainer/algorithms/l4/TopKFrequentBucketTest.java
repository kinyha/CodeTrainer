package trainer.algorithms.l4;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TopKFrequentBucketTest {

    @Test
    void returnsValuesOrderedByDescendingFrequency() {
        int[] values = {1, 1, 1, 2, 2, 3};
        assertThat(TopKFrequentBucket.topK(values, 2)).isEqualTo(List.of(1, 2));
    }

    @Test
    void kLargerThanDistinctValuesReturnsAllOfThem() {
        int[] values = {1, 1, 2};
        assertThat(TopKFrequentBucket.topK(values, 5)).containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void rejectsNonPositiveK() {
        assertThatIllegalArgumentException().isThrownBy(() -> TopKFrequentBucket.topK(new int[]{1}, 0));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> TopKFrequentBucket.topK(null, 1));
    }
}
