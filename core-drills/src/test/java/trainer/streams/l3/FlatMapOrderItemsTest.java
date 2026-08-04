package trainer.streams.l3;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderItem;
import trainer.fixtures.sales.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FlatMapOrderItemsTest {

    @Test
    void flattensOrdersAndAggregatesProductQuantity() {
        Order first = order(1, List.of(item(20, "Mouse", 1), item(10, "Keyboard", 2)));
        Order second = order(2, List.of(item(10, "Keyboard", 3)));

        assertThat(FlatMapOrderItems.summarize(List.of(first, second))).containsExactly(
                new FlatMapOrderItems.ProductQuantity(10, "Keyboard", 5),
                new FlatMapOrderItems.ProductQuantity(20, "Mouse", 1));
    }

    private static Order order(long id, List<OrderItem> items) {
        return new Order(id, 1, items, LocalDateTime.of(2024, 1, 1, 12, 0), OrderStatus.NEW);
    }

    private static OrderItem item(long id, String name, int quantity) {
        return new OrderItem(id, name, quantity, BigDecimal.TEN);
    }
}
