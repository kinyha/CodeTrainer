package trainer.sql.l3;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RankCustomersBySpendTest extends PostgresExerciseTestSupport {

    @Test
    void ranksCustomersByDeliveredSpend() throws Exception {
        List<String> result = new ArrayList<>();
        try (var connection = connection();
             var statement = connection.prepareStatement(RankCustomersBySpend.query());
             var rows = statement.executeQuery()) {
            while (rows.next()) {
                result.add(rows.getLong("customer_id") + ":" + rows.getBigDecimal("total_spend")
                        + ":" + rows.getLong("spend_rank"));
            }
        }

        assertThat(result).containsExactly("101:250.00:1", "102:225.00:2", "103:99.00:3", "104:40.00:4");
    }
}
