package trainer.sql.l2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@Testcontainers
class GroupByHavingOrdersTest {

    @Container
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17.6-alpine")
            .withDatabaseName("trainer")
            .withUsername("trainer")
            .withPassword("trainer")
            .withInitScript("sql/schema.sql");

    @BeforeAll
    static void seedDatabase() throws SQLException, IOException {
        try (Connection connection = connection();
             var stream = GroupByHavingOrdersTest.class.getResourceAsStream("/sql/seed.sql")) {
            if (stream == null) {
                throw new IllegalStateException("sql/seed.sql is missing");
            }
            connection.createStatement().execute(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    @Test
    void returnsOnlyCustomersMeetingDeliveredOrderThreshold() throws SQLException {
        List<CustomerOrders> result = new ArrayList<>();

        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(GroupByHavingOrders.query())) {
            statement.setInt(1, 2);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    result.add(new CustomerOrders(rows.getLong("customer_id"), rows.getLong("order_count")));
                }
            }
        }

        assertThat(result).containsExactly(
                new CustomerOrders(101, 3),
                new CustomerOrders(102, 2)
        );
    }

    private static Connection connection() throws SQLException {
        return DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    private record CustomerOrders(long customerId, long orderCount) {
    }
}
