package trainer.sql.l4;

import org.junit.jupiter.api.Test;
import trainer.sql.PostgresExerciseTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class SkipLockedJobClaimTest extends PostgresExerciseTestSupport {

    @Test
    void concurrentWorkersClaimDifferentRows() throws Exception {
        try (var first = connection(); var second = connection()) {
            first.setAutoCommit(false);
            second.setAutoCommit(false);
            try (var firstClaim = first.prepareStatement(SkipLockedJobClaim.query());
                 var secondClaim = second.prepareStatement(SkipLockedJobClaim.query());
                 var firstRow = firstClaim.executeQuery()) {
                assertThat(firstRow.next()).isTrue();
                assertThat(firstRow.getLong("id")).isEqualTo(201);
                try (var secondRow = secondClaim.executeQuery()) {
                    assertThat(secondRow.next()).isTrue();
                    assertThat(secondRow.getLong("id")).isEqualTo(202);
                }
            } finally {
                first.rollback();
                second.rollback();
            }
        }
    }
}
