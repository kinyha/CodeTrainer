package trainer.algorithms.l1;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CharFrequencyTest {

    @Test
    void countsEachCharacter() {
        assertThat(CharFrequency.count("aabbbc"))
                .containsExactly(Map.entry('a', 2), Map.entry('b', 3), Map.entry('c', 1));
    }

    @Test
    void preservesFirstSeenOrder() {
        assertThat(CharFrequency.count("baab").keySet()).containsExactly('b', 'a');
    }

    @Test
    void emptyStringGivesEmptyMap() {
        assertThat(CharFrequency.count("")).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CharFrequency.count(null));
    }
}
