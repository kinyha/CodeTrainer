package trainer.springcore.l1;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ComponentAndInjectionTest {

    @Test
    void springWiresTheSingleConstructorWithoutAutowiredAnnotation() {
        try (var context = new AnnotationConfigApplicationContext(
                ComponentAndInjection.DefaultGreeter.class, ComponentAndInjection.class)) {
            var service = context.getBean(ComponentAndInjection.class);
            assertThat(service.greet("Ada")).isEqualTo("Hello, Ada");
        }
    }

    @Test
    void rejectsNullDependencyDirectly() {
        assertThatNullPointerException().isThrownBy(() -> new ComponentAndInjection(null));
    }
}
