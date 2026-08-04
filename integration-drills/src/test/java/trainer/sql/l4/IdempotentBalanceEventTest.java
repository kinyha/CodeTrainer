package trainer.sql.l4;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class IdempotentBalanceEventTest extends PostgresExerciseTestSupport {

    @Test
    void appliesDuplicateEventExactlyOnce() throws Exception {
        try (var connection = connection()) {
            assertThat(apply(connection, "event-1", 501, new BigDecimal("10.00")))
                    .isEqualByComparingTo("110.00");
            assertThat(apply(connection, "event-1", 501, new BigDecimal("10.00"))).isNull();

            try (var statement = connection.prepareStatement(
                    "SELECT balance FROM account_balances WHERE account_id = 501");
                 var row = statement.executeQuery()) {
                assertThat(row.next()).isTrue();
                assertThat(row.getBigDecimal("balance")).isEqualByComparingTo("110.00");
            }
        }
    }

    private static BigDecimal apply(java.sql.Connection connection, String eventId, long accountId, BigDecimal delta)
            throws Exception {
        try (var statement = connection.prepareStatement(IdempotentBalanceEvent.query())) {
            statement.setString(1, eventId);
            statement.setLong(2, accountId);
            statement.setBigDecimal(3, delta);
            statement.setLong(4, accountId);
            try (var row = statement.executeQuery()) {
                return row.next() ? row.getBigDecimal("balance") : null;
            }
        }
    }
}
