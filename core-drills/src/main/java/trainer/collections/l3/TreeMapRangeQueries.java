package trainer.collections.l3;

import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

// @task collections.l3.TreeMapRangeQueries
// @tags TreeMap,NavigableMap,subMap,range
// @time 25m
// @src  new
public final class TreeMapRangeQueries {

    private TreeMapRangeQueries() {
    }

    /**
     * Значения по ключам в ЗАКРЫТОМ диапазоне [from, to] — оба конца включительно.
     * Двухаргументный subMap(from, to) исключает верхнюю границу; нужен 4-аргументный
     * overload с явными флагами включительности.
     */
    public static List<String> valuesInRange(TreeMap<Integer, String> byKey, int from, int to) {
        Objects.requireNonNull(byKey, "byKey");

        // ---8<--- solution
        if (to < from) {
            throw new IllegalArgumentException("to must not be before from");
        }
        return List.copyOf(byKey.subMap(from, true, to, true).values());
        // --->8--- solution
    }
}
