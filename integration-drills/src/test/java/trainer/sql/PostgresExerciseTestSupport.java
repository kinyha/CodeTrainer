package trainer.sql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Tag("integration")
@Testcontainers
public abstract class PostgresExerciseTestSupport {

    @Container
    protected static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17.6-alpine")
            .withDatabaseName("trainer")
            .withUsername("trainer")
            .withPassword("trainer")
            .withInitScript("sql/schema.sql");

    @BeforeAll
    static void seedDatabase() throws SQLException, IOException {
        try (Connection connection = connection();
             var stream = PostgresExerciseTestSupport.class.getResourceAsStream("/sql/seed.sql")) {
            if (stream == null) {
                throw new IllegalStateException("sql/seed.sql is missing");
            }
            connection.createStatement().execute(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    protected static Connection connection() throws SQLException {
        return DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }
}
