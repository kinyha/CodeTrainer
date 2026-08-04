package trainer.sql.l1;

// @task sql.l1.CountOrdersByStatus
// @tags sql,group-by,count,postgres,text-block
// @time 12m
// @src  new
public final class CountOrdersByStatus {

    private CountOrdersByStatus() {
    }

    /** Считает заказы по статусам и сортирует статусы лексикографически. */
    public static String query() {
        // ---8<--- solution
        return """
                SELECT status, COUNT(*) AS order_count
                FROM orders
                GROUP BY status
                ORDER BY status
                """;
        // --->8--- solution
    }
}
