package trainer.streams.l1;

import trainer.fixtures.sales.Transaction;
import trainer.fixtures.sales.TransactionStatus;

import java.util.List;
import java.util.Objects;

// @task streams.l1.CountCompleted
// @tags streams,filter,count,enum
// @time 6m
// @src  new
public final class CountCompleted {

    private CountCompleted() {
    }

    public static long count(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "transactions");

        // ---8<--- solution
        return transactions.stream()
                .peek(transaction -> Objects.requireNonNull(transaction, "transaction"))
                .filter(transaction -> transaction.status() == TransactionStatus.COMPLETED)
                .count();
        // --->8--- solution
    }
}
