package trainer.sql.l2;

// @task sql.l2.CustomersWithoutOrders
// @tags sql,left-join,anti-join,postgres,text-block
// @time 18m
// @src  new
public final class CustomersWithoutOrders {

    private CustomersWithoutOrders() {
    }

    /** Возвращает клиентов, у которых нет ни одного заказа. */
    public static String query() {
        // ---8<--- solution
        return """
                SELECT c.id, c.name
                FROM customers c
                LEFT JOIN orders o ON o.customer_id = c.id
                WHERE o.id IS NULL
                ORDER BY c.id
                """;
        // --->8--- solution
    }
}
