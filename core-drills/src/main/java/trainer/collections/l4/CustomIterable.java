package trainer.collections.l4;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

// @task collections.l4.CustomIterable
// @tags Iterable,Iterator,fail-fast,modCount
// @time 45m
// @src  new
public final class CustomIterable<T> implements Iterable<T> {

    private final List<T> values = new ArrayList<>();
    private int modCount = 0;

    public void add(T value) {
        Objects.requireNonNull(value, "value");
        values.add(value);
        modCount++;
    }

    /** fail-fast: любое add() после создания итератора рвёт итерацию ConcurrentModificationException. */
    @Override
    public Iterator<T> iterator() {
        // ---8<--- solution
        return new Iterator<>() {
            private final int expectedModCount = modCount;
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < values.size();
            }

            @Override
            public T next() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return values.get(index++);
            }
        };
        // --->8--- solution
    }

    int size() {
        return values.size();
    }
}
