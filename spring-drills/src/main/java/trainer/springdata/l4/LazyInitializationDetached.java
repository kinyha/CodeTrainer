package trainer.springdata.l4;

import java.util.List;
import java.util.Objects;

// @task springdata.l4.LazyInitializationDetached
// @tags spring-data,lazy-loading,detached-entity,session
// @time 30m
// @src  new
public final class LazyInitializationDetached {

    private LazyInitializationDetached() {
    }

    public record Customer(long id, String name, LazyCollection<String> orderIds) {
    }

    /** Имитирует ленивую коллекцию Hibernate: read() бросает, если сессия уже закрыта — так же,
     * как настоящий LazyInitializationException при обращении к detached-сущности. */
    public static final class LazyCollection<T> {
        private final List<T> values;
        private final boolean sessionOpen;

        public LazyCollection(List<T> values, boolean sessionOpen) {
            this.values = List.copyOf(values);
            this.sessionOpen = sessionOpen;
        }

        public List<T> read() {
            if (!sessionOpen) {
                throw new IllegalStateException("session is closed — orders were not eagerly fetched");
            }
            return values;
        }
    }

    /** Внутри транзакции читать лениво загруженную коллекцию безопасно — сессия ещё открыта. */
    public static int orderCountWithinSession(Customer customer) {
        Objects.requireNonNull(customer, "customer");

        // ---8<--- solution
        return customer.orderIds().read().size();
        // --->8--- solution
    }
}
