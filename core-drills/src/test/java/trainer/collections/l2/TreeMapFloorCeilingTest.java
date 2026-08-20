package trainer.collections.l2;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TreeMapFloorCeilingTest {

    private final TreeMap<Integer, String> tiers = new TreeMap<>(Map.of(0, "bronze", 100, "silver", 500, "gold"));

    @Test
    void picksTheHighestTierAtOrBelowAmount() {
        assertThat(TreeMapFloorCeiling.tierFor(tiers, 50)).contains("bronze");
        assertThat(TreeMapFloorCeiling.tierFor(tiers, 100)).contains("silver");
        assertThat(TreeMapFloorCeiling.tierFor(tiers, 499)).contains("silver");
        assertThat(TreeMapFloorCeiling.tierFor(tiers, 1000)).contains("gold");
    }

    @Test
    void belowTheLowestThresholdGivesEmpty() {
        assertThat(TreeMapFloorCeiling.tierFor(tiers, -1)).isEqualTo(Optional.empty());
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> TreeMapFloorCeiling.tierFor(null, 0));
    }
}
