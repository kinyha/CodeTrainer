package trainer.sql.l2;

// @task sql.l2.RunningTotalPerCustomer
// @tags sql,window-function,running-total,postgres
// @time 18m
// @src  new
public final class RunningTotalPerCustomer {

    private RunningTotalPerCustomer() {
    }

    /**
     * Накопительная сумма доставленных заказов по клиенту в хронологическом порядке.
     * PARTITION BY сбрасывает накопление на каждого клиента заново.
     */
    public static String query() {
        // ---8<--- solution
        return """
                SELECT customer_id, ordered_at, total,
                       SUM(total) OVER (PARTITION BY customer_id ORDER BY ordered_at) AS running_total
                FROM orders
                WHERE status = 'DELIVERED'
                ORDER BY customer_id, ordered_at
                """;
        // --->8--- solution
    }
}
