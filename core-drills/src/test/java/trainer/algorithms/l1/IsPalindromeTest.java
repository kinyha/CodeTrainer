package trainer.algorithms.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class IsPalindromeTest {

    @Test
    void ignoresCaseAndPunctuation() {
        assertThat(IsPalindrome.test("A man, a plan, a canal: Panama")).isTrue();
        assertThat(IsPalindrome.test("race a car")).isFalse();
    }

    @Test
    void emptyAndPunctuationOnlyValuesArePalindromes() {
        assertThat(IsPalindrome.test("")).isTrue();
        assertThat(IsPalindrome.test("... ---")).isTrue();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> IsPalindrome.test(null));
    }
}
