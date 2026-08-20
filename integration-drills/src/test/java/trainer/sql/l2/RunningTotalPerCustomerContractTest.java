package trainer.sql.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RunningTotalPerCustomerContractTest {

    @Test
    void keepsWindowFunctionOverDeliveredOrdersOnly() {
        String query = RunningTotalPerCustomer.query()
                .replaceAll("\\s+", " ")
                .trim();

        assertThat(query)
                .contains("WHERE status = 'DELIVERED'")
                .contains("SUM(total) OVER (PARTITION BY customer_id ORDER BY ordered_at)")
                .endsWith("ORDER BY customer_id, ordered_at");
    }
}
