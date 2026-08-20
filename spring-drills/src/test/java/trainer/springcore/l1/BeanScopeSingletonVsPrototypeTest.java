package trainer.springcore.l1;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScopeSingletonVsPrototypeTest {

    @Test
    void singletonBeansShareStateAcrossLookups() {
        try (var context = new AnnotationConfigApplicationContext(BeanScopeSingletonVsPrototype.Config.class)) {
            var first = context.getBean("singletonCounter", BeanScopeSingletonVsPrototype.Counter.class);
            var second = context.getBean("singletonCounter", BeanScopeSingletonVsPrototype.Counter.class);

            assertThat(first).isSameAs(second);
            assertThat(first.increment()).isEqualTo(1);
            assertThat(second.increment()).isEqualTo(2);
        }
    }

    @Test
    void prototypeBeansAreIndependentPerLookup() {
        try (var context = new AnnotationConfigApplicationContext(BeanScopeSingletonVsPrototype.Config.class)) {
            var first = context.getBean("prototypeCounter", BeanScopeSingletonVsPrototype.Counter.class);
            var second = context.getBean("prototypeCounter", BeanScopeSingletonVsPrototype.Counter.class);

            assertThat(first).isNotSameAs(second);
            assertThat(first.increment()).isEqualTo(1);
            assertThat(second.increment()).isEqualTo(1);
        }
    }
}
