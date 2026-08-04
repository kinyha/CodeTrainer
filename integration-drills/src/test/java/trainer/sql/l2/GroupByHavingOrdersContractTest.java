package trainer.sql.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroupByHavingOrdersContractTest {

    @Test
    void keepsFilteringGroupingAndThresholdInTheRightSqlPhases() {
        String query = GroupByHavingOrders.query()
                .replaceAll("\\s+", " ")
                .trim();

        assertThat(query)
                .contains("WHERE status = 'DELIVERED'")
                .contains("GROUP BY customer_id")
                .contains("HAVING COUNT(*) >= ?")
                .endsWith("ORDER BY order_count DESC, customer_id");
    }
}
