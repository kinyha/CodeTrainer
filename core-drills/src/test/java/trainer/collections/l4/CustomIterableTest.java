package trainer.collections.l4;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CustomIterableTest {

    @Test
    void iteratesInInsertionOrder() {
        CustomIterable<String> values = new CustomIterable<>();
        values.add("a");
        values.add("b");
        values.add("c");

        assertThat(values).containsExactly("a", "b", "c");
    }

    @Test
    void failsFastWhenMutatedDuringIteration() {
        CustomIterable<String> values = new CustomIterable<>();
        values.add("a");
        values.add("b");

        Iterator<String> iterator = values.iterator();
        iterator.next();
        values.add("c");

        assertThatExceptionOfType(ConcurrentModificationException.class).isThrownBy(iterator::next);
    }

    @Test
    void exhaustedIteratorThrowsNoSuchElement() {
        CustomIterable<String> values = new CustomIterable<>();
        values.add("only");

        Iterator<String> iterator = values.iterator();
        iterator.next();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(iterator::next);
    }
}
