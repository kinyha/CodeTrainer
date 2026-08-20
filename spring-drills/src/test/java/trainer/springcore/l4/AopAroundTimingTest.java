package trainer.springcore.l4;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class AopAroundTimingTest {

    @EnableAspectJAutoProxy
    static class Config {
    }

    @Component
    static class Greeter {
        @Timed
        String greet(String name) {
            return "hi " + name;
        }

        String greetUntimed(String name) {
            return "quiet hi " + name;
        }
    }

    @Test
    void wrapsOnlyAnnotatedMethodsAndReportsInvocation() {
        AtomicInteger measurements = new AtomicInteger();

        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(Config.class, Greeter.class);
            context.registerBean(AopAroundTiming.class, () -> new AopAroundTiming(nanos -> measurements.incrementAndGet()));
            context.refresh();

            var greeter = context.getBean(Greeter.class);

            assertThat(greeter.greet("Ada")).isEqualTo("hi Ada");
            assertThat(measurements.get()).isEqualTo(1);

            assertThat(greeter.greetUntimed("Bob")).isEqualTo("quiet hi Bob");
            assertThat(measurements.get()).isEqualTo(1);
        }
    }
}
