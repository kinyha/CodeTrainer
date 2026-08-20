package trainer.springdata.l5;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

// @task springdata.l5.PlaceOrderWithOutbox
// @tags spring-data,transactional-outbox,atomicity,event-driven
// @time 70m
// @src  new
// @doc  PlaceOrderWithOutbox.md
@Service
public final class PlaceOrderWithOutbox {

    private final OrderRepository orders;
    private final OutboxRepository outbox;
    private final Clock clock;
    private final Supplier<UUID> eventIds;

    public PlaceOrderWithOutbox(
            OrderRepository orders,
            OutboxRepository outbox,
            Clock clock,
            Supplier<UUID> eventIds
    ) {
        this.orders = Objects.requireNonNull(orders, "orders");
        this.outbox = Objects.requireNonNull(outbox, "outbox");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.eventIds = Objects.requireNonNull(eventIds, "eventIds");
    }

    /** Сохраняет aggregate и outbox event в одной локальной транзакции. */
    @Transactional
    public Order place(long orderId, long customerId, BigDecimal total) {
        Objects.requireNonNull(total, "total");

        // ---8<--- solution
        if (total.signum() <= 0) throw new IllegalArgumentException("total must be positive");
        Order order = new Order(orderId, customerId, total, "PLACED");
        orders.save(order);
        OutboxEvent event = new OutboxEvent(
                eventIds.get(),
                "Order",
                Long.toString(orderId),
                "OrderPlaced",
                "{\"orderId\":%d,\"customerId\":%d,\"total\":\"%s\"}"
                        .formatted(orderId, customerId, total.toPlainString()),
                clock.instant()
        );
        outbox.save(event);
        return order;
        // --->8--- solution
    }

    public record Order(long id, long customerId, BigDecimal total, String status) {
    }

    public record OutboxEvent(
            UUID id,
            String aggregateType,
            String aggregateId,
            String eventType,
            String payload,
            Instant occurredAt
    ) {
    }

    @FunctionalInterface
    public interface OrderRepository {
        void save(Order order);
    }

    @FunctionalInterface
    public interface OutboxRepository {
        void save(OutboxEvent event);
    }
}
