package trainer.algorithms.l1;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

// @task algorithms.l1.CharFrequency
// @tags strings,hash-map,frequency
// @time 8m
// @src  new
public final class CharFrequency {

    private CharFrequency() {
    }

    /** Считает частоту символов, сохраняя порядок их первого появления. */
    public static Map<Character, Integer> count(String value) {
        Objects.requireNonNull(value, "value");

        // ---8<--- solution
        Map<Character, Integer> frequency = new LinkedHashMap<>();
        for (int i = 0; i < value.length(); i++) {
            frequency.merge(value.charAt(i), 1, Integer::sum);
        }
        return frequency;
        // --->8--- solution
    }
}
