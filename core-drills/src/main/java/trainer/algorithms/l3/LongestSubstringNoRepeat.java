package trainer.algorithms.l3;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// @task algorithms.l3.LongestSubstringNoRepeat
// @tags strings,sliding-window,hash-map
// @time 25m
// @src  new
public final class LongestSubstringNoRepeat {

    private LongestSubstringNoRepeat() {
    }

    /** Длина самой длинной подстроки без повторяющихся символов. */
    public static int length(String value) {
        Objects.requireNonNull(value, "value");

        // ---8<--- solution
        Map<Character, Integer> lastSeenAt = new HashMap<>();
        int windowStart = 0;
        int best = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            Integer previous = lastSeenAt.get(c);
            if (previous != null && previous >= windowStart) {
                windowStart = previous + 1;
            }
            lastSeenAt.put(c, i);
            best = Math.max(best, i - windowStart + 1);
        }
        return best;
        // --->8--- solution
    }
}
