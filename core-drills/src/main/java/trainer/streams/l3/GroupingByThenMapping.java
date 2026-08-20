package trainer.streams.l3;

import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

// @task streams.l3.GroupingByThenMapping
// @tags streams,groupingBy,mapping,downstream
// @time 25m
// @src  new
public final class GroupingByThenMapping {

    private GroupingByThenMapping() {
    }

    /** Id заказов по статусу; mapping() применяет extractor уже ВНУТРИ groupingBy, без второго прохода. */
    public static Map<OrderStatus, List<Long>> orderIdsByStatus(List<Order> orders) {
        Objects.requireNonNull(orders, "orders");

        // ---8<--- solution
        return orders.stream()
                .peek(order -> Objects.requireNonNull(order, "order"))
                .collect(Collectors.groupingBy(
                        Order::status,
                        TreeMap::new,
                        Collectors.mapping(Order::id, Collectors.toList())
                ));
        // --->8--- solution
    }
}
