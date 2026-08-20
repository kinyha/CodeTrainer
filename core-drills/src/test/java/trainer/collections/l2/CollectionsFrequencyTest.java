package trainer.collections.l2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CollectionsFrequencyTest {

    @Test
    void findsTheClearMajorityValue() {
        assertThat(CollectionsFrequency.mostFrequent(List.of("a", "b", "a", "c", "a"))).isEqualTo("a");
    }

    @Test
    void breaksTiesByFirstAppearance() {
        assertThat(CollectionsFrequency.mostFrequent(List.of("a", "b", "a", "b"))).isEqualTo("a");
    }

    @Test
    void singleElementIsItsOwnAnswer() {
        assertThat(CollectionsFrequency.mostFrequent(List.of("only"))).isEqualTo("only");
    }

    @Test
    void rejectsEmptyList() {
        assertThatIllegalArgumentException().isThrownBy(() -> CollectionsFrequency.mostFrequent(List.of()));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CollectionsFrequency.mostFrequent(null));
    }
}
