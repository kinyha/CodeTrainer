package trainer.collections.l2;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

// @task collections.l2.TreeMapFloorCeiling
// @tags TreeMap,NavigableMap,floorKey,ceilingKey
// @time 12m
// @src  new
public final class TreeMapFloorCeiling {

    private TreeMapFloorCeiling() {
    }

    /** Ближайший тариф, действующий на amount: последний, чей порог <= amount. */
    public static Optional<String> tierFor(TreeMap<Integer, String> tiersByMinAmount, int amount) {
        Objects.requireNonNull(tiersByMinAmount, "tiersByMinAmount");

        // ---8<--- solution
        Integer key = tiersByMinAmount.floorKey(amount);
        return key == null ? Optional.empty() : Optional.of(tiersByMinAmount.get(key));
        // --->8--- solution
    }
}
