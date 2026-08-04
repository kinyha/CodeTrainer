package trainer.fixtures.sales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record Order(
        long id,
        long customerId,
        List<OrderItem> items,
        LocalDateTime orderedAt,
        OrderStatus status
) {
    public Order {
        items = List.copyOf(items);
        Objects.requireNonNull(orderedAt, "orderedAt");
        Objects.requireNonNull(status, "status");
    }

    public BigDecimal totalAmount() {
        return items.stream()
                .map(OrderItem::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
