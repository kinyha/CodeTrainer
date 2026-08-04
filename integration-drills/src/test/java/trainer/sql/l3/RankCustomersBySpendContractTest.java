package trainer.sql.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RankCustomersBySpendContractTest {

    @Test
    void usesAggregationAndDenseRankWindow() {
        assertThat(RankCustomersBySpend.query().toUpperCase())
                .contains("WITH SPEND AS", "SUM(TOTAL)", "DENSE_RANK() OVER", "ORDER BY TOTAL_SPEND DESC");
    }
}
