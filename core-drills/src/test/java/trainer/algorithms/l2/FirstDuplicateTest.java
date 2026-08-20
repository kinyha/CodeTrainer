package trainer.algorithms.l2;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class FirstDuplicateTest {

    @Test
    void findsValueWhoseSecondOccurrenceComesFirst() {
        assertThat(FirstDuplicate.find(new int[]{2, 1, 3, 5, 3, 2})).isEqualTo(OptionalInt.of(3));
    }

    @Test
    void noDuplicatesGivesEmptyOptional() {
        assertThat(FirstDuplicate.find(new int[]{1, 2, 3})).isEqualTo(OptionalInt.empty());
    }

    @Test
    void emptyArrayGivesEmptyOptional() {
        assertThat(FirstDuplicate.find(new int[0])).isEqualTo(OptionalInt.empty());
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> FirstDuplicate.find(null));
    }
}
