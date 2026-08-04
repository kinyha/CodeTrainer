package trainer.sql.l5;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveCategoryTreeTest extends PostgresExerciseTestSupport {

    @Test
    void returnsOnlyRequestedSubtreeWithDepthAndPath() throws Exception {
        List<String> rows = new ArrayList<>();
        try (var connection = connection();
             var statement = connection.prepareStatement(RecursiveCategoryTree.query())) {
            statement.setLong(1, 1);
            try (var result = statement.executeQuery()) {
                while (result.next()) {
                    rows.add(result.getLong("id") + ":" + result.getInt("depth") + ":" + result.getString("path"));
                }
            }
        }
        assertThat(rows).containsExactly(
                "1:0:Electronics",
                "3:1:Electronics > Laptops",
                "2:1:Electronics > Phones",
                "4:2:Electronics > Phones > Android");
    }
}
