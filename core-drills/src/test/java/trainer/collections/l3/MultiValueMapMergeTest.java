package trainer.collections.l3;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MultiValueMapMergeTest {

    @Test
    void concatenatesListsForSharedKeys() {
        Map<String, List<Integer>> first = Map.of("a", List.of(1, 2));
        Map<String, List<Integer>> second = Map.of("a", List.of(3));

        assertThat(MultiValueMapMerge.merge(first, second)).containsEntry("a", List.of(1, 2, 3));
    }

    @Test
    void keysUniqueToEitherSideArePreserved() {
        Map<String, List<Integer>> first = Map.of("a", List.of(1));
        Map<String, List<Integer>> second = Map.of("b", List.of(2));

        Map<String, List<Integer>> result = MultiValueMapMerge.merge(first, second);
        assertThat(result).containsEntry("a", List.of(1)).containsEntry("b", List.of(2));
    }

    @Test
    void bothEmptyGivesEmptyMap() {
        assertThat(MultiValueMapMerge.merge(Map.of(), Map.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> MultiValueMapMerge.merge(null, Map.of()));
        assertThatNullPointerException().isThrownBy(() -> MultiValueMapMerge.merge(Map.of(), null));
    }
}
