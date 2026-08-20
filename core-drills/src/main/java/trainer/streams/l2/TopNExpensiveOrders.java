package trainer.streams.l2;

import trainer.fixtures.sales.Order;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

// @task streams.l2.TopNExpensiveOrders
// @tags streams,sorted,limit,Comparator
// @time 12m
// @src  new
public final class TopNExpensiveOrders {

    private TopNExpensiveOrders() {
    }

    /** n самых дорогих заказов по убыванию суммы. */
    public static List<Order> topN(List<Order> orders, int n) {
        Objects.requireNonNull(orders, "orders");

        // ---8<--- solution
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative");
        }
        return orders.stream()
                .peek(order -> Objects.requireNonNull(order, "order"))
                .sorted(Comparator.comparing(Order::totalAmount).reversed())
                .limit(n)
                .toList();
        // --->8--- solution
    }
}
