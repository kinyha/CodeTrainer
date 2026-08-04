package trainer.collections.l1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.entry;

class MapGetOrDefaultTest {

    @Test
    void countsDuplicatesInFirstSeenOrder() {
        assertThat(MapGetOrDefault.frequencies(List.of("java", "kotlin", "java", "sql", "kotlin", "java")))
                .containsExactly(
                        entry("java", 3),
                        entry("kotlin", 2),
                        entry("sql", 1)
                );
    }

    @Test
    void handlesEmptyInput() {
        assertThat(MapGetOrDefault.frequencies(List.of())).isEmpty();
    }

    @Test
    void rejectsNullList() {
        assertThatNullPointerException()
                .isThrownBy(() -> MapGetOrDefault.frequencies(null))
                .withMessage("words");
    }
}
