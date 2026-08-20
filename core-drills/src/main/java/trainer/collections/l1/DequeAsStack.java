package trainer.collections.l1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

// @task collections.l1.DequeAsStack
// @tags Deque,ArrayDeque,stack
// @time 7m
// @src  new
public final class DequeAsStack {

    private DequeAsStack() {
    }

    /** Разворачивает список через ArrayDeque в роли стека. */
    public static <T> List<T> reverse(List<T> values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        Deque<T> stack = new ArrayDeque<>();
        for (T value : values) {
            stack.push(value);
        }
        List<T> result = new ArrayList<>(values.size());
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
        // --->8--- solution
    }
}
