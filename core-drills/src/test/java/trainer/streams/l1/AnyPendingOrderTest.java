package trainer.streams.l1;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class AnyPendingOrderTest {

    @Test
    void trueWhenAtLeastOneOrderIsPending() {
        assertThat(AnyPendingOrder.exists(List.of(order(OrderStatus.DELIVERED), order(OrderStatus.PROCESSING)))).isTrue();
    }

    @Test
    void falseWhenNoOrderIsPending() {
        assertThat(AnyPendingOrder.exists(List.of(order(OrderStatus.DELIVERED), order(OrderStatus.CANCELLED)))).isFalse();
    }

    @Test
    void emptyListIsFalse() {
        assertThat(AnyPendingOrder.exists(List.of())).isFalse();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> AnyPendingOrder.exists(null));
    }

    private static Order order(OrderStatus status) {
        return new Order(1, 1, List.of(), LocalDateTime.of(2024, 1, 1, 12, 0), status);
    }
}
