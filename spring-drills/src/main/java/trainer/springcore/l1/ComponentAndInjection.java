package trainer.springcore.l1;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

// @task springcore.l1.ComponentAndInjection
// @tags spring-core,constructor-injection,fail-fast
// @time 10m
// @src  new
@Service
public final class ComponentAndInjection {

    private final Greeter greeter;

    /**
     * Constructor injection — единственный конструктор Spring находит и вызывает сам,
     * @Autowired на нём не обязателен. Без зависимости объект вообще нельзя создать — это
     * и есть fail-fast: ошибка конфигурации всплывает при старте контекста, а не в рантайме.
     */
    public ComponentAndInjection(Greeter greeter) {
        // ---8<--- solution
        this.greeter = Objects.requireNonNull(greeter, "greeter");
        // --->8--- solution
    }

    public String greet(String name) {
        return greeter.greet(name);
    }

    @FunctionalInterface
    public interface Greeter {
        String greet(String name);
    }

    @Component
    public static final class DefaultGreeter implements Greeter {
        @Override
        public String greet(String name) {
            return "Hello, " + name;
        }
    }
}
