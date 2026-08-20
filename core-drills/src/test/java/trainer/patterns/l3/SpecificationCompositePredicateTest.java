package trainer.patterns.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class SpecificationCompositePredicateTest {

    private final SpecificationCompositePredicate.Specification<Integer> isPositive = value -> value > 0;
    private final SpecificationCompositePredicate.Specification<Integer> isEven = value -> value % 2 == 0;

    @Test
    void andRequiresBothSpecificationsToMatch() {
        var positiveAndEven = isPositive.and(isEven);

        assertThat(positiveAndEven.isSatisfiedBy(4)).isTrue();
        assertThat(positiveAndEven.isSatisfiedBy(3)).isFalse();
        assertThat(positiveAndEven.isSatisfiedBy(-4)).isFalse();
    }

    @Test
    void originalSpecificationsStayIndependentlyUsable() {
        isPositive.and(isEven);
        assertThat(isPositive.isSatisfiedBy(3)).isTrue();
        assertThat(isEven.isSatisfiedBy(4)).isTrue();
    }

    @Test
    void negateInvertsTheResult() {
        var isOdd = isEven.negate();
        assertThat(isOdd.isSatisfiedBy(3)).isTrue();
        assertThat(isOdd.isSatisfiedBy(4)).isFalse();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> isPositive.and(null));
    }
}
