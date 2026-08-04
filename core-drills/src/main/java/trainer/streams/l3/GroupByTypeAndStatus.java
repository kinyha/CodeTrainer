package trainer.streams.l3;

import trainer.fixtures.sales.Transaction;
import trainer.fixtures.sales.TransactionStatus;
import trainer.fixtures.sales.TransactionType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// @task streams.l3.GroupByTypeAndStatus
// @tags streams,groupingBy,downstream,EnumMap
// @time 25m
// @src  new
// @doc  GroupByTypeAndStatus.md
public final class GroupByTypeAndStatus {

    private GroupByTypeAndStatus() {
    }

    public static Map<TransactionType, Map<TransactionStatus, Long>> count(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "transactions");

        // ---8<--- solution
        return transactions.stream()
                .peek(transaction -> Objects.requireNonNull(transaction, "transaction"))
                .collect(Collectors.groupingBy(
                        Transaction::type,
                        () -> new EnumMap<>(TransactionType.class),
                        Collectors.groupingBy(
                                Transaction::status,
                                () -> new EnumMap<>(TransactionStatus.class),
                                Collectors.counting()
                        )
                ));
        // --->8--- solution
    }
}
