package trainer.streams.l1;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Transaction;
import trainer.fixtures.sales.TransactionStatus;
import trainer.fixtures.sales.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CountCompletedTest {

    @Test
    void countsOnlyCompletedTransactions() {
        assertThat(CountCompleted.count(List.of(tx(1, TransactionStatus.COMPLETED),
                tx(2, TransactionStatus.FAILED), tx(3, TransactionStatus.COMPLETED)))).isEqualTo(2);
        assertThat(CountCompleted.count(List.of())).isZero();
    }

    private static Transaction tx(long id, TransactionStatus status) {
        return new Transaction(id, 1, BigDecimal.TEN, TransactionType.PAYMENT, status,
                LocalDateTime.of(2024, 1, 1, 12, 0), "shop");
    }
}
