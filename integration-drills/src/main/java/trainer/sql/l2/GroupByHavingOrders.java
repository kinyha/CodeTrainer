package trainer.sql.l2;

// @task sql.l2.GroupByHavingOrders
// @tags sql,group-by,having,postgres,text-block
// @time 20m
// @src  LeetCode-75-Study-Project:docs/java-interview-tasks-catalog.md#sql
// @doc  GroupByHavingOrders.md
public final class GroupByHavingOrders {

    private GroupByHavingOrders() {
    }

    /**
     * Возвращает PostgreSQL-запрос для клиентов с не менее чем заданным числом
     * доставленных заказов. Порог передаётся первым параметром PreparedStatement.
     * Результат сортируется по убыванию количества и затем по customer_id.
     */
    public static String query() {
        // ---8<--- solution
        return """
                SELECT customer_id, COUNT(*) AS order_count
                FROM orders
                WHERE status = 'DELIVERED'
                GROUP BY customer_id
                HAVING COUNT(*) >= ?
                ORDER BY order_count DESC, customer_id
                """;
        // --->8--- solution
    }
}
