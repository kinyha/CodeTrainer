package trainer.collections.l1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ListRemoveWhileIteratingTest {

    @Test
    void removesConsecutiveMatchesWithoutConcurrentModification() {
        List<Integer> values = new ArrayList<>(List.of(1, 2, 4, 5, 6));

        assertThat(ListRemoveWhileIterating.remove(values, value -> value % 2 == 0)).isEqualTo(3);
        assertThat(values).containsExactly(1, 5);
    }

    @Test
    void handlesEmptyMutableList() {
        List<String> values = new ArrayList<>();
        assertThat(ListRemoveWhileIterating.remove(values, String::isBlank)).isZero();
    }
}
