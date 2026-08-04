package trainer.collections.l1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @task collections.l1.MapGetOrDefault
// @tags Map,getOrDefault,frequency,LinkedHashMap
// @time 7m
// @src  new
public final class MapGetOrDefault {

    private MapGetOrDefault() {
    }

    /**
     * Считает частоту слов, сохраняя порядок их первого появления.
     * Контракт: список и его элементы не могут быть null; пустой список даёт пустую Map.
     */
    public static Map<String, Integer> frequencies(List<String> words) {
        Objects.requireNonNull(words, "words");

        // ---8<--- solution
        Map<String, Integer> frequencies = new LinkedHashMap<>();
        for (String word : words) {
            Objects.requireNonNull(word, "word");
            frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        }
        return frequencies;
        // --->8--- solution
    }
}
