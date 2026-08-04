package trainer.sql.l4;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SkipLockedJobClaimContractTest {

    @Test
    void locksOneReadyJobWithoutWaiting() {
        assertThat(SkipLockedJobClaim.query().toUpperCase())
                .contains("STATUS = 'READY'", "FOR UPDATE SKIP LOCKED", "LIMIT 1");
    }
}
