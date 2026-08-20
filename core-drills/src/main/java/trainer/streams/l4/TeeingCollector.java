package trainer.streams.l4;

import trainer.fixtures.sales.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// @task streams.l4.TeeingCollector
// @tags streams,teeing,Collectors,two-results
// @time 45m
// @src  new
public final class TeeingCollector {

    private TeeingCollector() {
    }

    /** Collectors.teeing прогоняет данные через ДВА коллектора за один проход и сводит результаты. */
    public static Summary summarize(List<Order> orders) {
        Objects.requireNonNull(orders, "orders");

        // ---8<--- solution
        return orders.stream()
                .peek(order -> Objects.requireNonNull(order, "order"))
                .collect(Collectors.teeing(
                        Collectors.counting(),
                        Collectors.reducing(BigDecimal.ZERO, Order::totalAmount, BigDecimal::add),
                        Summary::new
                ));
        // --->8--- solution
    }

    public record Summary(long count, BigDecimal totalAmount) {
    }
}
