package trainer.algorithms.l1;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class FindMaxElementTest {

    @Test
    void findsMaxAmongMixedValues() {
        assertThat(FindMaxElement.max(new int[]{3, -7, 9, 2})).isEqualTo(OptionalInt.of(9));
    }

    @Test
    void singleElementIsItsOwnMax() {
        assertThat(FindMaxElement.max(new int[]{5})).isEqualTo(OptionalInt.of(5));
    }

    @Test
    void emptyArrayGivesEmptyOptional() {
        assertThat(FindMaxElement.max(new int[0])).isEqualTo(OptionalInt.empty());
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> FindMaxElement.max(null));
    }
}
