package trainer.concurrency.l4;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// @task concurrency.l4.ConcurrentHashMapAtomicCompose
// @tags concurrency,ConcurrentHashMap,compute,atomicity
// @time 45m
// @src  new
public final class ConcurrentHashMapAtomicCompose {

    private final ConcurrentMap<String, Integer> counts = new ConcurrentHashMap<>();

    /**
     * get-then-put — это ДВЕ операции: между ними другой поток может вклиниться и потерять
     * обновление. compute() атомарен для данного ключа целиком, ручная блокировка не нужна.
     */
    public int incrementAndGet(String key) {
        Objects.requireNonNull(key, "key");

        // ---8<--- solution
        return counts.compute(key, (ignored, current) -> current == null ? 1 : current + 1);
        // --->8--- solution
    }

    public int get(String key) {
        return counts.getOrDefault(key, 0);
    }
}
