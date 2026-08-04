package trainer.sql.l4;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdempotentBalanceEventContractTest {

    @Test
    void claimsEventAndUpdatesBalanceInOneStatement() {
        assertThat(IdempotentBalanceEvent.query().toUpperCase())
                .contains("INSERT INTO PROCESSED_EVENTS", "ON CONFLICT (EVENT_ID) DO NOTHING",
                        "UPDATE ACCOUNT_BALANCES", "EXISTS (SELECT 1 FROM CLAIMED)", "RETURNING BALANCE");
    }
}
