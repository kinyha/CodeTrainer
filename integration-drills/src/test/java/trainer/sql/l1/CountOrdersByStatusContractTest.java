package trainer.sql.l1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountOrdersByStatusContractTest {

    @Test
    void containsAggregationAndDeterministicOrder() {
        assertThat(CountOrdersByStatus.query().toUpperCase())
                .contains("COUNT(*)", "GROUP BY STATUS", "ORDER BY STATUS");
    }
}
