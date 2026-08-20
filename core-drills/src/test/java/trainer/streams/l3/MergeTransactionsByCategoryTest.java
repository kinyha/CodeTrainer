package trainer.streams.l3;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Transaction;
import trainer.fixtures.sales.TransactionStatus;
import trainer.fixtures.sales.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MergeTransactionsByCategoryTest {

    @Test
    void sumsAmountsPerCategory() {
        List<Transaction> transactions = List.of(
                transaction("groceries", "10"), transaction("groceries", "5"), transaction("rent", "100"));

        assertThat(MergeTransactionsByCategory.totalByCategory(transactions)).containsExactly(
                Map.entry("groceries", new BigDecimal("15")), Map.entry("rent", new BigDecimal("100")));
    }

    @Test
    void emptyListGivesEmptyMap() {
        assertThat(MergeTransactionsByCategory.totalByCategory(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> MergeTransactionsByCategory.totalByCategory(null));
    }

    private static Transaction transaction(String category, String amount) {
        return new Transaction(1, 1, new BigDecimal(amount), TransactionType.PAYMENT, TransactionStatus.COMPLETED,
                LocalDateTime.of(2024, 1, 1, 12, 0), category);
    }
}
