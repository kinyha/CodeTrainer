package trainer.sql.l2;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RunningTotalPerCustomerTest extends PostgresExerciseTestSupport {

    @Test
    void accumulatesTotalPerCustomerInChronologicalOrder() throws Exception {
        List<String> result = new ArrayList<>();
        try (var connection = connection();
             var statement = connection.prepareStatement(RunningTotalPerCustomer.query());
             var rows = statement.executeQuery()) {
            while (rows.next()) {
                result.add(rows.getLong("customer_id") + ":" + rows.getBigDecimal("running_total"));
            }
        }

        assertThat(result).containsExactly(
                "101:120.00", "101:200.00", "101:250.00",
                "102:200.00", "102:225.00",
                "103:99.00",
                "104:40.00");
    }
}
