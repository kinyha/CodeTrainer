package trainer.collections.l1;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class SetIntersectionTest {

    @Test
    void returnsCommonElementsOnly() {
        assertThat(SetIntersection.intersect(Set.of(1, 2, 3), Set.of(2, 3, 4)))
                .containsExactlyInAnyOrder(2, 3);
    }

    @Test
    void disjointSetsGiveEmptyResult() {
        assertThat(SetIntersection.intersect(Set.of(1, 2), Set.of(3, 4))).isEmpty();
    }

    @Test
    void doesNotMutateEitherArgument() {
        Set<Integer> first = Set.of(1, 2);
        Set<Integer> second = Set.of(2, 3);

        SetIntersection.intersect(first, second);

        assertThat(first).containsExactlyInAnyOrder(1, 2);
        assertThat(second).containsExactlyInAnyOrder(2, 3);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> SetIntersection.intersect(null, Set.of()));
        assertThatNullPointerException().isThrownBy(() -> SetIntersection.intersect(Set.of(), null));
    }
}
