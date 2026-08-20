package trainer.streams.l1;

import trainer.fixtures.sales.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

// @task streams.l1.SumOrderTotals
// @tags streams,reduce,BigDecimal
// @time 7m
// @src  new
public final class SumOrderTotals {

    private SumOrderTotals() {
    }

    public static BigDecimal sum(List<Order> orders) {
        Objects.requireNonNull(orders, "orders");

        // ---8<--- solution
        return orders.stream()
                .peek(order -> Objects.requireNonNull(order, "order"))
                .map(Order::totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // --->8--- solution
    }
}
