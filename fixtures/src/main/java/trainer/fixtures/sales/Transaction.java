package trainer.fixtures.sales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Transaction(
        long id,
        long customerId,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        LocalDateTime occurredAt,
        String category
) {
    public Transaction {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(occurredAt, "occurredAt");
        Objects.requireNonNull(category, "category");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
    }
}
