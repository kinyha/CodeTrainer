package trainer.springcore.l1;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

// @task springcore.l1.BeanScopeSingletonVsPrototype
// @tags spring-core,bean-scope,singleton,prototype
// @time 10m
// @src  new
public final class BeanScopeSingletonVsPrototype {

    private BeanScopeSingletonVsPrototype() {
    }

    public static final class Counter {
        private int value;

        /** У singleton-бина value копится между вызовами; у prototype каждый getBean() даёт новый объект с value=0. */
        public int increment() {
            // ---8<--- solution
            return ++value;
            // --->8--- solution
        }
    }

    @Configuration
    public static class Config {

        @Bean
        public Counter singletonCounter() {
            return new Counter();
        }

        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        public Counter prototypeCounter() {
            return new Counter();
        }
    }
}
