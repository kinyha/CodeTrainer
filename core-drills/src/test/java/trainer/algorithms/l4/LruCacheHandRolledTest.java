package trainer.algorithms.l4;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class LruCacheHandRolledTest {

    @Test
    void evictsLeastRecentlyUsedWhenFull() {
        LruCacheHandRolled cache = new LruCacheHandRolled(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30); // вытесняет ключ 1

        assertThat(cache.get(1)).isEqualTo(-1);
        assertThat(cache.get(2)).isEqualTo(20);
        assertThat(cache.get(3)).isEqualTo(30);
    }

    @Test
    void getRefreshesRecency() {
        LruCacheHandRolled cache = new LruCacheHandRolled(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.get(1); // 1 становится самым свежим, 2 - самым старым
        cache.put(3, 30); // вытесняет ключ 2

        assertThat(cache.get(2)).isEqualTo(-1);
        assertThat(cache.get(1)).isEqualTo(10);
        assertThat(cache.get(3)).isEqualTo(30);
    }

    @Test
    void putOnExistingKeyUpdatesValueAndRecency() {
        LruCacheHandRolled cache = new LruCacheHandRolled(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(1, 100); // обновляет значение и делает 1 самым свежим
        cache.put(3, 30); // вытесняет ключ 2, а не 1

        assertThat(cache.get(1)).isEqualTo(100);
        assertThat(cache.get(2)).isEqualTo(-1);
    }

    @Test
    void rejectsNonPositiveCapacity() {
        assertThatIllegalArgumentException().isThrownBy(() -> new LruCacheHandRolled(0));
    }
}
