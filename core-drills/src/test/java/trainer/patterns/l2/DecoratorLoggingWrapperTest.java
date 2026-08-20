package trainer.patterns.l2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DecoratorLoggingWrapperTest {

    @Test
    void logsEachCallWithoutChangingTheResult() {
        List<String> log = new ArrayList<>();
        var doubled = DecoratorLoggingWrapper.withLogging(value -> value * 2, log);

        assertThat(doubled.applyAsInt(3)).isEqualTo(6);
        assertThat(doubled.applyAsInt(5)).isEqualTo(10);
        assertThat(log).containsExactly("3 -> 6", "5 -> 10");
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> DecoratorLoggingWrapper.withLogging(null, new ArrayList<>()));
        assertThatNullPointerException().isThrownBy(() -> DecoratorLoggingWrapper.withLogging(value -> value, null));
    }
}
