package trainer.collections.l1;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// @task collections.l1.SetIntersection
// @tags Set,retainAll,intersection
// @time 6m
// @src  new
public final class SetIntersection {

    private SetIntersection() {
    }

    /** Возвращает новое множество, не изменяя ни один из аргументов. */
    public static <T> Set<T> intersect(Set<T> first, Set<T> second) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");

        // ---8<--- solution
        Set<T> result = new HashSet<>(first);
        result.retainAll(second);
        return result;
        // --->8--- solution
    }
}
