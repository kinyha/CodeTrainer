package trainer.sql.l2;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomersWithoutOrdersTest extends PostgresExerciseTestSupport {

    @Test
    void returnsOnlyCustomerWithNoOrders() throws Exception {
        List<String> result = new ArrayList<>();
        try (var connection = connection();
             var statement = connection.prepareStatement(CustomersWithoutOrders.query());
             var rows = statement.executeQuery()) {
            while (rows.next()) {
                result.add(rows.getLong("id") + ":" + rows.getString("name"));
            }
        }

        assertThat(result).containsExactly("105:Eve");
    }
}
