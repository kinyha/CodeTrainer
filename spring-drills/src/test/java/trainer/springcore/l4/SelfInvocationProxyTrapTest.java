package trainer.springcore.l4;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class SelfInvocationProxyTrapTest {

    @EnableAspectJAutoProxy
    static class Config {
    }

    @Test
    void selfInvokedInnerCallBypassesTheProxyAndSkipsItsOwnAdvice() {
        AtomicInteger measurements = new AtomicInteger();

        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(Config.class, SelfInvocationProxyTrap.class);
            context.registerBean(AopAroundTiming.class, () -> new AopAroundTiming(nanos -> measurements.incrementAndGet()));
            context.refresh();

            String result = context.getBean(SelfInvocationProxyTrap.class).outer("Ada");

            assertThat(result).isEqualTo("hi Ada");
            assertThat(measurements.get()).isEqualTo(1); // только outer(); inner() вызван мимо proxy
        }
    }

    @Test
    void callingInnerDirectlyFromOutsideIsAdvisedNormally() {
        AtomicInteger measurements = new AtomicInteger();

        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(Config.class, SelfInvocationProxyTrap.class);
            context.registerBean(AopAroundTiming.class, () -> new AopAroundTiming(nanos -> measurements.incrementAndGet()));
            context.refresh();

            context.getBean(SelfInvocationProxyTrap.class).inner("Bob");

            assertThat(measurements.get()).isEqualTo(1);
        }
    }
}
