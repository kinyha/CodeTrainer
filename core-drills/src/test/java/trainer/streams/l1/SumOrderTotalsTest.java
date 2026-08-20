package trainer.streams.l1;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderItem;
import trainer.fixtures.sales.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class SumOrderTotalsTest {

    @Test
    void sumsTotalsAcrossOrders() {
        Order first = order(List.of(item(2, BigDecimal.TEN)));
        Order second = order(List.of(item(1, BigDecimal.valueOf(5))));

        assertThat(SumOrderTotals.sum(List.of(first, second))).isEqualByComparingTo("25");
    }

    @Test
    void emptyListSumsToZero() {
        assertThat(SumOrderTotals.sum(List.of())).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> SumOrderTotals.sum(null));
    }

    private static Order order(List<OrderItem> items) {
        return new Order(1, 1, items, LocalDateTime.of(2024, 1, 1, 12, 0), OrderStatus.NEW);
    }

    private static OrderItem item(int quantity, BigDecimal price) {
        return new OrderItem(1, "Widget", quantity, price);
    }
}
