package trainer.streams.l3;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class GroupingByThenMappingTest {

    @Test
    void groupsOrderIdsByStatusInEnumOrder() {
        var result = GroupingByThenMapping.orderIdsByStatus(List.of(
                order(1, OrderStatus.SHIPPED), order(2, OrderStatus.NEW), order(3, OrderStatus.SHIPPED)));

        assertThat(result.keySet()).containsExactly(OrderStatus.NEW, OrderStatus.SHIPPED);
        assertThat(result.get(OrderStatus.SHIPPED)).containsExactly(1L, 3L);
        assertThat(result.get(OrderStatus.NEW)).containsExactly(2L);
    }

    @Test
    void emptyListGivesEmptyMap() {
        assertThat(GroupingByThenMapping.orderIdsByStatus(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> GroupingByThenMapping.orderIdsByStatus(null));
    }

    private static Order order(long id, OrderStatus status) {
        return new Order(id, 1, List.of(), LocalDateTime.of(2024, 1, 1, 12, 0), status);
    }
}
