package trainer.collections.l1;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

// @task collections.l1.ListRemoveWhileIterating
// @tags List,Iterator,remove,mutation
// @time 8m
// @src  new
public final class ListRemoveWhileIterating {

    private ListRemoveWhileIterating() {
    }

    /** Удаляет элементы на месте и возвращает их количество. */
    public static <T> int remove(List<T> values, Predicate<? super T> predicate) {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(predicate, "predicate");

        // ---8<--- solution
        int removed = 0;
        Iterator<T> iterator = values.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                iterator.remove();
                removed++;
            }
        }
        return removed;
        // --->8--- solution
    }
}
