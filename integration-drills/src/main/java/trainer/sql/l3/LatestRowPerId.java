package trainer.sql.l3;

// @task sql.l3.LatestRowPerId
// @tags sql,distinct-on,postgres,latest-row
// @time 25m
// @src  new
public final class LatestRowPerId {

    private LatestRowPerId() {
    }

    /**
     * DISTINCT ON (customer_id) — постгресовое расширение: оставляет первую строку в каждой
     * группе customer_id согласно ORDER BY. Здесь "первая" — самая свежая по ordered_at,
     * поэтому DESC внутри ORDER BY обязателен, а сам customer_id должен идти первым в ORDER BY.
     */
    public static String query() {
        // ---8<--- solution
        return """
                SELECT DISTINCT ON (customer_id) customer_id, id AS order_id, status, ordered_at
                FROM orders
                ORDER BY customer_id, ordered_at DESC
                """;
        // --->8--- solution
    }
}
