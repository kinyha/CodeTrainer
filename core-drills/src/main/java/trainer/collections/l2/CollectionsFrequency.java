package trainer.collections.l2;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

// @task collections.l2.CollectionsFrequency
// @tags Collections,frequency,max
// @time 10m
// @src  new
public final class CollectionsFrequency {

    private CollectionsFrequency() {
    }

    /** Значение, встречающееся чаще всего; при равенстве — первое из таких по порядку списка. */
    public static <T> T mostFrequent(List<T> values) {
        Objects.requireNonNull(values, "values");

        // ---8<--- solution
        if (values.isEmpty()) {
            throw new IllegalArgumentException("values must not be empty");
        }
        return Collections.max(values, Comparator.comparingInt(value -> Collections.frequency(values, value)));
        // --->8--- solution
    }
}
