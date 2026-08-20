package trainer.springcore.l3;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// @task springcore.l3.ApplicationEventListenerDecoupling
// @tags spring-core,application-event,event-listener,decoupling
// @time 22m
// @src  new
public final class ApplicationEventListenerDecoupling {

    private ApplicationEventListenerDecoupling() {
    }

    public record OrderPlaced(long orderId) {
    }

    public static final class OrderService {
        private final ApplicationEventPublisher publisher;

        public OrderService(ApplicationEventPublisher publisher) {
            this.publisher = Objects.requireNonNull(publisher, "publisher");
        }

        /** OrderService не знает и не должен знать, КТО слушает событие и сколько таких слушателей. */
        public void place(long orderId) {
            // ---8<--- solution
            publisher.publishEvent(new OrderPlaced(orderId));
            // --->8--- solution
        }
    }

    @Component
    public static final class OrderPlacedAuditListener {
        private final List<Long> auditedOrderIds = new ArrayList<>();

        @EventListener
        public void onOrderPlaced(OrderPlaced event) {
            // ---8<--- solution
            auditedOrderIds.add(event.orderId());
            // --->8--- solution
        }

        public List<Long> auditedOrderIds() {
            return List.copyOf(auditedOrderIds);
        }
    }
}
