package trainer.patterns.l3;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

// @task patterns.l3.FactoryMethodRegistry
// @tags patterns,factory-method,registry,open-closed
// @time 25m
// @src  new
public final class FactoryMethodRegistry {

    private final Map<String, Supplier<Object>> factories;

    public FactoryMethodRegistry(Map<String, Supplier<Object>> factories) {
        this.factories = Map.copyOf(Objects.requireNonNull(factories, "factories"));
    }

    /**
     * Реестр открыт для расширения без изменения этого класса: новый тип регистрируется
     * снаружи, добавлением записи в Map, а не новой веткой if/switch здесь.
     */
    public Object create(String type) {
        Objects.requireNonNull(type, "type");

        // ---8<--- solution
        Supplier<Object> factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("unknown type: " + type);
        }
        return factory.get();
        // --->8--- solution
    }
}
