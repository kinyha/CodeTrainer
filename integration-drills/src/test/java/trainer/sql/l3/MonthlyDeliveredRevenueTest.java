package trainer.sql.l3;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class MonthlyDeliveredRevenueTest extends PostgresExerciseTestSupport {

    @Test
    void aggregatesOnlyDeliveredOrders() throws Exception {
        Map<LocalDate, BigDecimal> result = new LinkedHashMap<>();
        try (var connection = connection();
             var statement = connection.prepareStatement(MonthlyDeliveredRevenue.query());
             var rows = statement.executeQuery()) {
            while (rows.next()) {
                result.put(rows.getObject("month", LocalDate.class), rows.getBigDecimal("revenue"));
            }
        }

        assertThat(result).containsExactly(
                entry(LocalDate.of(2026, 6, 1), new BigDecimal("574.00")),
                entry(LocalDate.of(2026, 7, 1), new BigDecimal("40.00")));
    }
}
