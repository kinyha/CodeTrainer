package trainer.algorithms.l3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class GroupAnagramsTest {

    @Test
    void groupsAnagramsByFirstAppearance() {
        List<String> words = List.of("eat", "tea", "tan", "ate", "nat", "bat");

        assertThat(GroupAnagrams.group(words)).containsExactly(
                List.of("eat", "tea", "ate"),
                List.of("tan", "nat"),
                List.of("bat"));
    }

    @Test
    void emptyListGivesEmptyList() {
        assertThat(GroupAnagrams.group(List.of())).isEmpty();
    }

    @Test
    void wordWithNoAnagramGetsItsOwnGroup() {
        assertThat(GroupAnagrams.group(List.of("solo"))).containsExactly(List.of("solo"));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> GroupAnagrams.group(null));
    }
}
