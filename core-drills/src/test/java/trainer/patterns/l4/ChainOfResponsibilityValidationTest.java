package trainer.patterns.l4;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ChainOfResponsibilityValidationTest {

    private final ChainOfResponsibilityValidation.Validator notBlank =
            input -> input.isBlank() ? Optional.of("must not be blank") : Optional.empty();
    private final ChainOfResponsibilityValidation.Validator maxLength =
            input -> input.length() > 10 ? Optional.of("too long") : Optional.empty();

    @Test
    void validInputPassesEveryValidator() {
        assertThat(ChainOfResponsibilityValidation.run("hello", List.of(notBlank, maxLength))).isEmpty();
    }

    @Test
    void stopsAtTheFirstFailingValidator() {
        AtomicInteger secondValidatorCalls = new AtomicInteger();
        ChainOfResponsibilityValidation.Validator spySecond = input -> {
            secondValidatorCalls.incrementAndGet();
            return Optional.empty();
        };

        Optional<String> result = ChainOfResponsibilityValidation.run("", List.of(notBlank, spySecond));

        assertThat(result).contains("must not be blank");
        assertThat(secondValidatorCalls.get()).isZero();
    }

    @Test
    void reachesLaterValidatorsWhenEarlierOnesPass() {
        assertThat(ChainOfResponsibilityValidation.run("way too long input", List.of(notBlank, maxLength)))
                .contains("too long");
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ChainOfResponsibilityValidation.run(null, List.of()));
        assertThatNullPointerException().isThrownBy(() -> ChainOfResponsibilityValidation.run("x", null));
    }
}
