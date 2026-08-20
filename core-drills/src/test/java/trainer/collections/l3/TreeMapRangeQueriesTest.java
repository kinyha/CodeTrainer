package trainer.collections.l3;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TreeMapRangeQueriesTest {

    private final TreeMap<Integer, String> byKey = new TreeMap<>(Map.of(1, "a", 3, "b", 5, "c", 7, "d"));

    @Test
    void includesBothEndpointsOfTheRange() {
        assertThat(TreeMapRangeQueries.valuesInRange(byKey, 3, 7)).containsExactly("b", "c", "d");
    }

    @Test
    void rangeCoveringNothingGivesEmptyList() {
        assertThat(TreeMapRangeQueries.valuesInRange(byKey, 10, 20)).isEmpty();
    }

    @Test
    void equalFromAndToMatchesExactlyOneKey() {
        assertThat(TreeMapRangeQueries.valuesInRange(byKey, 3, 3)).containsExactly("b");
    }

    @Test
    void rejectsInvertedRange() {
        assertThatIllegalArgumentException().isThrownBy(() -> TreeMapRangeQueries.valuesInRange(byKey, 5, 1));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> TreeMapRangeQueries.valuesInRange(null, 0, 1));
    }
}
