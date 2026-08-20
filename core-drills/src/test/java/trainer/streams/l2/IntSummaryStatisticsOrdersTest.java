package trainer.streams.l2;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.OrderItem;

import java.math.BigDecimal;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class IntSummaryStatisticsOrdersTest {

    @Test
    void computesMinMaxSumCount() {
        IntSummaryStatistics stats = IntSummaryStatisticsOrders.quantityStats(List.of(
                item(2), item(5), item(3)));

        assertThat(stats.getCount()).isEqualTo(3);
        assertThat(stats.getSum()).isEqualTo(10);
        assertThat(stats.getMin()).isEqualTo(2);
        assertThat(stats.getMax()).isEqualTo(5);
    }

    @Test
    void emptyListGivesZeroCount() {
        IntSummaryStatistics stats = IntSummaryStatisticsOrders.quantityStats(List.of());

        assertThat(stats.getCount()).isZero();
        assertThat(stats.getSum()).isZero();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> IntSummaryStatisticsOrders.quantityStats(null));
    }

    private static OrderItem item(int quantity) {
        return new OrderItem(1, "Widget", quantity, BigDecimal.ONE);
    }
}
