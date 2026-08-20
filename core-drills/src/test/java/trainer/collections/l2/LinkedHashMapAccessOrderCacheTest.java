package trainer.collections.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class LinkedHashMapAccessOrderCacheTest {

    @Test
    void evictsLeastRecentlyUsedWhenOverCapacity() {
        var cache = new LinkedHashMapAccessOrderCache<Integer, String>(2);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c"); // вытесняет 1

        assertThat(cache).doesNotContainKey(1);
        assertThat(cache).containsKeys(2, 3);
    }

    @Test
    void getRefreshesRecency() {
        var cache = new LinkedHashMapAccessOrderCache<Integer, String>(2);
        cache.put(1, "a");
        cache.put(2, "b");
        cache.get(1); // 1 становится свежим, 2 - самым старым
        cache.put(3, "c"); // вытесняет 2

        assertThat(cache).doesNotContainKey(2);
        assertThat(cache).containsKeys(1, 3);
    }

    @Test
    void rejectsNonPositiveCapacity() {
        assertThatIllegalArgumentException().isThrownBy(() -> new LinkedHashMapAccessOrderCache<Integer, String>(0));
    }
}
