package trainer.streams.l2;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderItem;
import trainer.fixtures.sales.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TopNExpensiveOrdersTest {

    @Test
    void returnsMostExpensiveOrdersFirst() {
        Order cheap = order(1, "10");
        Order mid = order(2, "50");
        Order expensive = order(3, "100");

        assertThat(TopNExpensiveOrders.topN(List.of(cheap, expensive, mid), 2))
                .containsExactly(expensive, mid);
    }

    @Test
    void nLargerThanSizeReturnsEverythingSorted() {
        Order cheap = order(1, "10");
        Order expensive = order(2, "100");

        assertThat(TopNExpensiveOrders.topN(List.of(cheap, expensive), 5)).containsExactly(expensive, cheap);
    }

    @Test
    void zeroReturnsEmptyList() {
        assertThat(TopNExpensiveOrders.topN(List.of(order(1, "10")), 0)).isEmpty();
    }

    @Test
    void rejectsNegativeN() {
        assertThatIllegalArgumentException().isThrownBy(() -> TopNExpensiveOrders.topN(List.of(), -1));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> TopNExpensiveOrders.topN(null, 1));
    }

    private static Order order(long id, String price) {
        return new Order(id, 1, List.of(new OrderItem(1, "Widget", 1, new BigDecimal(price))),
                LocalDateTime.of(2024, 1, 1, 12, 0), OrderStatus.NEW);
    }
}
