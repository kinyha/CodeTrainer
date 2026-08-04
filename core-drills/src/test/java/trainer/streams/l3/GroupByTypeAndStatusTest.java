package trainer.streams.l3;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Transaction;
import trainer.fixtures.sales.TransactionStatus;
import trainer.fixtures.sales.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class GroupByTypeAndStatusTest {

    @Test
    void createsTwoLevelEnumIndex() {
        var result = GroupByTypeAndStatus.count(List.of(
                tx(1, TransactionType.PAYMENT, TransactionStatus.COMPLETED),
                tx(2, TransactionType.PAYMENT, TransactionStatus.COMPLETED),
                tx(3, TransactionType.PAYMENT, TransactionStatus.FAILED),
                tx(4, TransactionType.REFUND, TransactionStatus.COMPLETED)));

        assertThat(result.get(TransactionType.PAYMENT)).containsOnly(
                entry(TransactionStatus.COMPLETED, 2L), entry(TransactionStatus.FAILED, 1L));
        assertThat(result.get(TransactionType.REFUND)).containsOnly(entry(TransactionStatus.COMPLETED, 1L));
    }

    private static Transaction tx(long id, TransactionType type, TransactionStatus status) {
        return new Transaction(id, 1, BigDecimal.TEN, type, status,
                LocalDateTime.of(2024, 1, 1, 12, 0), "shop");
    }
}
