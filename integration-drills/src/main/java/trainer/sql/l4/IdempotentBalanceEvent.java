package trainer.sql.l4;

// @task sql.l4.IdempotentBalanceEvent
// @tags sql,idempotency,on-conflict,cte,exactly-once-effect
// @time 50m
// @src  new
// @doc  IdempotentBalanceEvent.md
public final class IdempotentBalanceEvent {

    private IdempotentBalanceEvent() {
    }

    /**
     * Атомарно применяет delta один раз на event_id.
     * Параметры: event_id, account_id для claim, delta, account_id для update.
     */
    public static String query() {
        // ---8<--- solution
        return """
                WITH claimed AS (
                    INSERT INTO processed_events (event_id)
                    SELECT ?
                    WHERE EXISTS (
                        SELECT 1 FROM account_balances WHERE account_id = ?
                    )
                    ON CONFLICT (event_id) DO NOTHING
                    RETURNING event_id
                )
                UPDATE account_balances
                SET balance = balance + ?
                WHERE account_id = ?
                  AND EXISTS (SELECT 1 FROM claimed)
                RETURNING balance
                """;
        // --->8--- solution
    }
}
