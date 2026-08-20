package trainer.springcore.l3;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.List;
import java.util.Objects;

// @task springcore.l3.BeanLifecycleCallbacks
// @tags spring-core,lifecycle,post-construct,pre-destroy
// @time 20m
// @src  new
public final class BeanLifecycleCallbacks {

    private final List<String> events;
    private final ConnectionPool pool;

    public BeanLifecycleCallbacks(List<String> events, ConnectionPool pool) {
        this.events = Objects.requireNonNull(events, "events");
        this.pool = Objects.requireNonNull(pool, "pool");
    }

    /** @PostConstruct вызывается ПОСЛЕ инъекции всех зависимостей, но ДО того, как бин отдадут вызывающему коду. */
    @PostConstruct
    public void open() {
        // ---8<--- solution
        pool.open();
        events.add("opened");
        // --->8--- solution
    }

    /** @PreDestroy вызывается при закрытии контекста — самое место освобождать ресурсы. */
    @PreDestroy
    public void close() {
        // ---8<--- solution
        pool.close();
        events.add("closed");
        // --->8--- solution
    }

    public interface ConnectionPool {
        void open();

        void close();
    }
}
