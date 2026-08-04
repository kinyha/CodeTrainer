package trainer.springdata.l4;

// @task springdata.l4.NPlusOneFetchJoin
// @tags spring-data,jpa,n-plus-one,join-fetch,hibernate
// @time 45m
// @src  LeetCode-75-Study-Project:docs/spring-boot-jpa-guide.md
// @doc  NPlusOneFetchJoin.md
public final class NPlusOneFetchJoin {

    private NPlusOneFetchJoin() {
    }

    /** Загружает клиентов вместе с заказами одним запросом без дубликатов root entity. */
    public static String jpql() {
        // ---8<--- solution
        return """
                SELECT DISTINCT customer
                FROM CustomerEntity customer
                LEFT JOIN FETCH customer.orders
                ORDER BY customer.id
                """;
        // --->8--- solution
    }
}
