package trainer.sql.l3;

// @task sql.l3.RankCustomersBySpend
// @tags sql,cte,window-function,dense-rank,postgres
// @time 30m
// @src  new
// @doc  RankCustomersBySpend.md
public final class RankCustomersBySpend {

    private RankCustomersBySpend() {
    }

    /** Ранжирует клиентов по сумме доставленных заказов без разрывов в рангах. */
    public static String query() {
        // ---8<--- solution
        return """
                WITH spend AS (
                    SELECT customer_id, SUM(total) AS total_spend
                    FROM orders
                    WHERE status = 'DELIVERED'
                    GROUP BY customer_id
                )
                SELECT customer_id, total_spend,
                       DENSE_RANK() OVER (ORDER BY total_spend DESC) AS spend_rank
                FROM spend
                ORDER BY spend_rank, customer_id
                """;
        // --->8--- solution
    }
}
