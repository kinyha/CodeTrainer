package trainer.algorithms.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ValidBracketsTest {

    @Test
    void acceptsProperlyNestedBrackets() {
        assertThat(ValidBrackets.isValid("{[]()}")).isTrue();
    }

    @Test
    void rejectsCrossedBrackets() {
        assertThat(ValidBrackets.isValid("([)]")).isFalse();
    }

    @Test
    void rejectsUnmatchedClosingBracket() {
        assertThat(ValidBrackets.isValid("(]")).isFalse();
    }

    @Test
    void rejectsUnclosedOpeningBracket() {
        assertThat(ValidBrackets.isValid("(")).isFalse();
    }

    @Test
    void rejectsUnknownCharacters() {
        assertThat(ValidBrackets.isValid("(a)")).isFalse();
    }

    @Test
    void emptyStringIsValid() {
        assertThat(ValidBrackets.isValid("")).isTrue();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ValidBrackets.isValid(null));
    }
}
