package trainer.sql.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LatestRowPerIdContractTest {

    @Test
    void ordersByCustomerThenNewestFirstWithDistinctOnCustomer() {
        String query = LatestRowPerId.query()
                .replaceAll("\\s+", " ")
                .trim();

        assertThat(query)
                .contains("DISTINCT ON (customer_id)")
                .endsWith("ORDER BY customer_id, ordered_at DESC");
    }
}
