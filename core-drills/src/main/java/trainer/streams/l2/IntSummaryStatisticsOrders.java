package trainer.streams.l2;

import trainer.fixtures.sales.OrderItem;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;

// @task streams.l2.IntSummaryStatisticsOrders
// @tags streams,mapToInt,IntSummaryStatistics
// @time 10m
// @src  new
public final class IntSummaryStatisticsOrders {

    private IntSummaryStatisticsOrders() {
    }

    /** min/max/avg/sum по количеству товара за один проход; пустой список — не исключение, а нули. */
    public static IntSummaryStatistics quantityStats(List<OrderItem> items) {
        Objects.requireNonNull(items, "items");

        // ---8<--- solution
        return items.stream()
                .peek(item -> Objects.requireNonNull(item, "item"))
                .mapToInt(OrderItem::quantity)
                .summaryStatistics();
        // --->8--- solution
    }
}
