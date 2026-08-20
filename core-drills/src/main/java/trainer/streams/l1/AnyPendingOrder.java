package trainer.streams.l1;

import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderStatus;

import java.util.List;
import java.util.Objects;
import java.util.Set;

// @task streams.l1.AnyPendingOrder
// @tags streams,anyMatch,short-circuit
// @time 6m
// @src  new
public final class AnyPendingOrder {

    private static final Set<OrderStatus> PENDING = Set.of(OrderStatus.NEW, OrderStatus.PROCESSING);

    private AnyPendingOrder() {
    }

    /** anyMatch останавливается на первом совпадении — не проходит весь список без нужды. */
    public static boolean exists(List<Order> orders) {
        Objects.requireNonNull(orders, "orders");

        // ---8<--- solution
        return orders.stream()
                .peek(order -> Objects.requireNonNull(order, "order"))
                .anyMatch(order -> PENDING.contains(order.status()));
        // --->8--- solution
    }
}
