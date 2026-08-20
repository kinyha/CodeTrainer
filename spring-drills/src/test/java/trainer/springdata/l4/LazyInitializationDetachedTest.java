package trainer.springdata.l4;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class LazyInitializationDetachedTest {

    @Test
    void readsTheCollectionWhileTheSessionIsOpen() {
        var customer = new LazyInitializationDetached.Customer(1, "Ada",
                new LazyInitializationDetached.LazyCollection<>(List.of("o1", "o2"), true));

        assertThat(LazyInitializationDetached.orderCountWithinSession(customer)).isEqualTo(2);
    }

    @Test
    void failsToReadTheCollectionAfterTheSessionIsClosed() {
        var customer = new LazyInitializationDetached.Customer(1, "Ada",
                new LazyInitializationDetached.LazyCollection<>(List.of("o1", "o2"), false));

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> LazyInitializationDetached.orderCountWithinSession(customer));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> LazyInitializationDetached.orderCountWithinSession(null));
    }
}
