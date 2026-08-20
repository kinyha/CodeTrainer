package trainer.collections.l2;

import java.util.LinkedHashMap;
import java.util.Map;

// @task collections.l2.LinkedHashMapAccessOrderCache
// @tags LinkedHashMap,access-order,removeEldestEntry,lru
// @time 15m
// @src  new
public final class LinkedHashMapAccessOrderCache<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public LinkedHashMapAccessOrderCache(int capacity) {
        super(16, 0.75f, true); // accessOrder=true: get() тоже двигает запись в конец
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    /** Готовый крючок LinkedHashMap: вызывается после каждого put/putAll. */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // ---8<--- solution
        return size() > capacity;
        // --->8--- solution
    }
}
