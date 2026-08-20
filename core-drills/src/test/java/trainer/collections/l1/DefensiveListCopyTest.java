package trainer.collections.l1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DefensiveListCopyTest {

    @Test
    void copyHasTheSameElements() {
        assertThat(DefensiveListCopy.copy(List.of(1, 2, 3))).containsExactly(1, 2, 3);
    }

    @Test
    void laterMutationOfSourceDoesNotAffectCopy() {
        List<Integer> source = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> copy = DefensiveListCopy.copy(source);
        source.add(4);

        assertThat(copy).containsExactly(1, 2, 3);
    }

    @Test
    void copyRejectsMutation() {
        List<Integer> copy = DefensiveListCopy.copy(List.of(1, 2));
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> copy.add(3));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> DefensiveListCopy.copy(null));
    }
}
