package trainer.collections.l3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @task collections.l3.MultiValueMapMerge
// @tags Map,merge,multimap,computeIfAbsent
// @time 25m
// @src  new
public final class MultiValueMapMerge {

    private MultiValueMapMerge() {
    }

    /** Склеивает списки по общим ключам; ключи, которых нет в other, копируются как есть. */
    public static Map<String, List<Integer>> merge(Map<String, List<Integer>> first, Map<String, List<Integer>> second) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");

        // ---8<--- solution
        Map<String, List<Integer>> result = new LinkedHashMap<>();
        first.forEach((key, values) -> result.put(key, new ArrayList<>(values)));
        second.forEach((key, values) -> result.computeIfAbsent(key, ignored -> new ArrayList<>()).addAll(values));
        return result;
        // --->8--- solution
    }
}
