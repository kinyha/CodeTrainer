package trainer.springcore.l3;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BeanLifecycleCallbacksTest {

    @Test
    void callsPostConstructOnStartupAndPreDestroyOnShutdown() {
        List<String> events = new ArrayList<>();
        BeanLifecycleCallbacks.ConnectionPool pool = new BeanLifecycleCallbacks.ConnectionPool() {
            @Override
            public void open() {
            }

            @Override
            public void close() {
            }
        };

        var context = new AnnotationConfigApplicationContext();
        context.registerBean(BeanLifecycleCallbacks.class, () -> new BeanLifecycleCallbacks(events, pool));
        context.refresh();

        assertThat(events).containsExactly("opened");

        context.close();

        assertThat(events).containsExactly("opened", "closed");
    }
}
