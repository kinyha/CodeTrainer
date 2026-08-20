package trainer.collections.l4;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HashMapMutableKeyTest {

    @Test
    void survivesMutationOfTheOriginalKeyAfterPut() {
        HashMapMutableKey<String> map = new HashMapMutableKey<>();
        List<Integer> key = new ArrayList<>(List.of(1, 2, 3));

        map.put(key, "value");
        key.add(99); // ключ мутирует уже после put

        assertThat(map.get(List.of(1, 2, 3))).isEqualTo("value");
        assertThat(map.size()).isEqualTo(1);
    }

    @Test
    void lookupWithEqualButDifferentKeyInstanceWorks() {
        HashMapMutableKey<String> map = new HashMapMutableKey<>();
        map.put(new ArrayList<>(List.of(1, 2)), "a");

        assertThat(map.get(new ArrayList<>(List.of(1, 2)))).isEqualTo("a");
    }

    @Test
    void missingKeyReturnsNull() {
        HashMapMutableKey<String> map = new HashMapMutableKey<>();
        assertThat(map.get(List.of(7))).isNull();
    }
}
