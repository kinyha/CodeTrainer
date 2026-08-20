package trainer.patterns.l4;

import java.util.Objects;
import java.util.function.Supplier;

// @task patterns.l4.ProxyLazyInitialization
// @tags patterns,proxy,lazy-init,thread-safe
// @time 35m
// @src  new
public final class ProxyLazyInitialization<T> {

    private final Supplier<T> factory;
    private volatile T instance;

    public ProxyLazyInitialization(Supplier<T> factory) {
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    /**
     * Proxy откладывает создание дорогого объекта до первого реального обращения.
     * Синхронизация только на пути создания — уже созданный instance читается без блокировки.
     */
    public T get() {
        // ---8<--- solution
        T result = instance;
        if (result == null) {
            synchronized (this) {
                result = instance;
                if (result == null) {
                    instance = result = factory.get();
                }
            }
        }
        return result;
        // --->8--- solution
    }
}
