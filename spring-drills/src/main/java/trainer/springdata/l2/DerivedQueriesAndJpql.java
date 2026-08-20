package trainer.springdata.l2;

// @task springdata.l2.DerivedQueriesAndJpql
// @tags spring-data,jpql,derived-query,query-method
// @time 15m
// @src  new
public final class DerivedQueriesAndJpql {

    private DerivedQueriesAndJpql() {
    }

    /**
     * Эквивалент derived-метода findByStatusAndCustomerIdOrderByOrderedAtDesc(status, customerId):
     * Spring Data сам разбирает имя метода на WHERE + ORDER BY — здесь то же самое явным JPQL.
     */
    public static String jpql() {
        // ---8<--- solution
        return """
                SELECT o FROM OrderEntity o
                WHERE o.status = :status AND o.customerId = :customerId
                ORDER BY o.orderedAt DESC
                """;
        // --->8--- solution
    }
}
