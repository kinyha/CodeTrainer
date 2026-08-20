package trainer.springcore.l3;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationEventListenerDecouplingTest {

    @Test
    void listenerReceivesEventPublishedByTheService() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(ApplicationEventListenerDecoupling.OrderPlacedAuditListener.class);
            context.registerBean(ApplicationEventListenerDecoupling.OrderService.class,
                    () -> new ApplicationEventListenerDecoupling.OrderService(context));
            context.refresh();

            context.getBean(ApplicationEventListenerDecoupling.OrderService.class).place(42);

            var listener = context.getBean(ApplicationEventListenerDecoupling.OrderPlacedAuditListener.class);
            assertThat(listener.auditedOrderIds()).containsExactly(42L);
        }
    }
}
