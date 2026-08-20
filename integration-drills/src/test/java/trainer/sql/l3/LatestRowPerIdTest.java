package trainer.sql.l3;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LatestRowPerIdTest extends PostgresExerciseTestSupport {

    @Test
    void returnsTheMostRecentOrderPerCustomer() throws Exception {
        List<String> result = new ArrayList<>();
        try (var connection = connection();
             var statement = connection.prepareStatement(LatestRowPerId.query());
             var rows = statement.executeQuery()) {
            while (rows.next()) {
                result.add(rows.getLong("customer_id") + ":" + rows.getLong("order_id") + ":" + rows.getString("status"));
            }
        }

        assertThat(result).containsExactly(
                "101:3:DELIVERED",
                "102:5:DELIVERED",
                "103:7:CANCELLED",
                "104:9:DELIVERED");
    }
}
