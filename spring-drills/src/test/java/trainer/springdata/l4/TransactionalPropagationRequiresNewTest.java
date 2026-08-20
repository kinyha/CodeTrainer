package trainer.springdata.l4;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TransactionalPropagationRequiresNewTest {

    private static final Instant NOW = Instant.parse("2026-08-04T12:00:00Z");

    private final TransactionalPropagationRequiresNew.AuditLogRepository auditLog = mock();
    private final TransactionalPropagationRequiresNew service =
            new TransactionalPropagationRequiresNew(auditLog, Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void savesAnAuditEntryWithTheCurrentTime() {
        service.record("order.cancelled");
        verify(auditLog).save(new TransactionalPropagationRequiresNew.AuditEntry("order.cancelled", NOW));
    }

    @Test
    void declaresRequiresNewPropagation() throws Exception {
        var method = TransactionalPropagationRequiresNew.class.getMethod("record", String.class);
        Transactional annotation = method.getAnnotation(Transactional.class);
        assertThat(annotation).isNotNull();
        assertThat(annotation.propagation()).isEqualTo(Propagation.REQUIRES_NEW);
    }
}
