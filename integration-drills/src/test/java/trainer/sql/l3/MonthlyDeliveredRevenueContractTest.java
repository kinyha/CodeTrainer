package trainer.sql.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MonthlyDeliveredRevenueContractTest {

    @Test
    void filtersDeliveredAndGroupsByMonth() {
        assertThat(MonthlyDeliveredRevenue.query().toUpperCase())
                .contains("DATE_TRUNC('MONTH'", "SUM(TOTAL)", "STATUS = 'DELIVERED'", "GROUP BY");
    }
}
