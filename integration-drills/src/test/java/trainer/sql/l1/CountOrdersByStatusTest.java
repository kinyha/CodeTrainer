package trainer.sql.l1;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class CountOrdersByStatusTest extends PostgresExerciseTestSupport {

    @Test
    void countsEveryStatus() throws Exception {
        Map<String, Long> result = new LinkedHashMap<>();
        try (var connection = connection();
             var statement = connection.prepareStatement(CountOrdersByStatus.query());
             ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                result.put(rows.getString("status"), rows.getLong("order_count"));
            }
        }

        assertThat(result).containsExactly(
                entry("CANCELLED", 1L), entry("DELIVERED", 7L), entry("NEW", 1L));
    }
}
