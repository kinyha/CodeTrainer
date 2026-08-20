package trainer.algorithms.l3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @task algorithms.l3.GroupAnagrams
// @tags strings,hash-map,sorting
// @time 25m
// @src  new
public final class GroupAnagrams {

    private GroupAnagrams() {
    }

    /** Группирует анаграммы вместе; порядок групп — по первому появлению слова этой группы. */
    public static List<List<String>> group(List<String> words) {
        Objects.requireNonNull(words, "words");

        // ---8<--- solution
        Map<String, List<String>> byKey = new LinkedHashMap<>();
        for (String word : words) {
            Objects.requireNonNull(word, "word");
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            String key = new String(letters);
            byKey.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
        }
        return List.copyOf(byKey.values());
        // --->8--- solution
    }
}
