package trainer.sql.l3;

// @task sql.l3.MonthlyDeliveredRevenue
// @tags sql,date-trunc,sum,group-by,postgres
// @time 25m
// @src  new
// @doc  MonthlyDeliveredRevenue.md
public final class MonthlyDeliveredRevenue {

    private MonthlyDeliveredRevenue() {
    }

    /** Суммирует доставленную выручку по календарным месяцам. */
    public static String query() {
        // ---8<--- solution
        return """
                SELECT DATE_TRUNC('month', ordered_at)::date AS month,
                       SUM(total) AS revenue
                FROM orders
                WHERE status = 'DELIVERED'
                GROUP BY DATE_TRUNC('month', ordered_at)
                ORDER BY month
                """;
        // --->8--- solution
    }
}
