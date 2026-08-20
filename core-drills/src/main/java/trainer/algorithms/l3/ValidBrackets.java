package trainer.algorithms.l3;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;

// @task algorithms.l3.ValidBrackets
// @tags strings,stack,matching
// @time 25m
// @src  new
public final class ValidBrackets {

    private static final Map<Character, Character> CLOSING_TO_OPENING = Map.of(')', '(', ']', '[', '}', '{');

    private ValidBrackets() {
    }

    /** Поддерживает (), [] и {}; любой другой символ считается невалидным вводом. */
    public static boolean isValid(String value) {
        Objects.requireNonNull(value, "value");

        // ---8<--- solution
        Deque<Character> openBrackets = new ArrayDeque<>();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (CLOSING_TO_OPENING.containsValue(c)) {
                openBrackets.push(c);
            } else if (CLOSING_TO_OPENING.containsKey(c)) {
                if (openBrackets.isEmpty() || !openBrackets.pop().equals(CLOSING_TO_OPENING.get(c))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return openBrackets.isEmpty();
        // --->8--- solution
    }
}
