package trainer.springdata.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DerivedQueriesAndJpqlTest {

    @Test
    void filtersByStatusAndCustomerOrderedByNewestFirst() {
        String jpql = DerivedQueriesAndJpql.jpql().replaceAll("\\s+", " ").trim();

        assertThat(jpql)
                .contains("WHERE o.status = :status AND o.customerId = :customerId")
                .endsWith("ORDER BY o.orderedAt DESC");
    }
}
