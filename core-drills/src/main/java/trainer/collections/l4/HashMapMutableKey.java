package trainer.collections.l4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @task collections.l4.HashMapMutableKey
// @tags HashMap,mutable-key,defensive-copy,hashCode
// @time 45m
// @src  new
public final class HashMapMutableKey<V> {

    private final Map<List<Integer>, V> byKey = new HashMap<>();

    /**
     * Мутабельный List — плохой ключ HashMap: если его поменять после put, hashCode
     * "уедет" в другой бакет и запись станет ненаходимой. Защита — снимок ключа на входе.
     */
    public void put(List<Integer> key, V value) {
        Objects.requireNonNull(key, "key");

        // ---8<--- solution
        byKey.put(List.copyOf(key), value);
        // --->8--- solution
    }

    public V get(List<Integer> key) {
        Objects.requireNonNull(key, "key");
        return byKey.get(List.copyOf(key));
    }

    int size() {
        return byKey.size();
    }
}
