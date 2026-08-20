package trainer.collections.l2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriorityQueueTopKTest {

    @Test
    void returnsKLargestInAscendingOrder() {
        assertThat(PriorityQueueTopK.topK(List.of(3, 1, 4, 1, 5, 9, 2, 6), 3)).containsExactly(5, 6, 9);
    }

    @Test
    void kEqualToSizeReturnsEverythingSorted() {
        assertThat(PriorityQueueTopK.topK(List.of(3, 1, 2), 3)).containsExactly(1, 2, 3);
    }

    @Test
    void rejectsNonPositiveK() {
        assertThatIllegalArgumentException().isThrownBy(() -> PriorityQueueTopK.topK(List.of(1), 0));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> PriorityQueueTopK.topK(null, 1));
    }
}
