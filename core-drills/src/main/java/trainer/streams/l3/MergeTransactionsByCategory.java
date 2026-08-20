package trainer.streams.l3;

import trainer.fixtures.sales.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

// @task streams.l3.MergeTransactionsByCategory
// @tags streams,groupingBy,reducing,BigDecimal
// @time 25m
// @src  new
public final class MergeTransactionsByCategory {

    private MergeTransactionsByCategory() {
    }

    public static Map<String, BigDecimal> totalByCategory(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "transactions");

        // ---8<--- solution
        return transactions.stream()
                .peek(transaction -> Objects.requireNonNull(transaction, "transaction"))
                .collect(Collectors.groupingBy(
                        Transaction::category,
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)
                ));
        // --->8--- solution
    }
}
