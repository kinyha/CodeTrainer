package trainer.collections.l1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DequeAsStackTest {

    @Test
    void reversesOrder() {
        assertThat(DequeAsStack.reverse(List.of(1, 2, 3))).containsExactly(3, 2, 1);
    }

    @Test
    void singleElementStaysTheSame() {
        assertThat(DequeAsStack.reverse(List.of(42))).containsExactly(42);
    }

    @Test
    void emptyListStaysEmpty() {
        assertThat(DequeAsStack.reverse(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> DequeAsStack.reverse(null));
    }
}
