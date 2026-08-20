package trainer.streams.l4;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Order;
import trainer.fixtures.sales.OrderItem;
import trainer.fixtures.sales.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TeeingCollectorTest {

    @Test
    void computesCountAndTotalInOnePass() {
        Order first = order("10");
        Order second = order("25");

        TeeingCollector.Summary summary = TeeingCollector.summarize(List.of(first, second));

        assertThat(summary.count()).isEqualTo(2);
        assertThat(summary.totalAmount()).isEqualByComparingTo("35");
    }

    @Test
    void emptyListGivesZeroedSummary() {
        TeeingCollector.Summary summary = TeeingCollector.summarize(List.of());

        assertThat(summary.count()).isZero();
        assertThat(summary.totalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> TeeingCollector.summarize(null));
    }

    private static Order order(String price) {
        return new Order(1, 1, List.of(new OrderItem(1, "Widget", 1, new BigDecimal(price))),
                LocalDateTime.of(2024, 1, 1, 12, 0), OrderStatus.NEW);
    }
}
