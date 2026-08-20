package trainer.algorithms.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class LongestSubstringNoRepeatTest {

    @Test
    void findsLongestWindowWithoutRepeats() {
        assertThat(LongestSubstringNoRepeat.length("abcabcbb")).isEqualTo(3);
    }

    @Test
    void allSameCharacterGivesWindowOfOne() {
        assertThat(LongestSubstringNoRepeat.length("bbbbb")).isEqualTo(1);
    }

    @Test
    void windowCanStartAfterFirstFewCharacters() {
        assertThat(LongestSubstringNoRepeat.length("pwwkew")).isEqualTo(3);
    }

    @Test
    void emptyStringGivesZero() {
        assertThat(LongestSubstringNoRepeat.length("")).isZero();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> LongestSubstringNoRepeat.length(null));
    }
}
