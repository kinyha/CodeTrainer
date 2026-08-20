package trainer.springdata.l4;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

// @task springdata.l4.TransactionalPropagationRequiresNew
// @tags spring-data,transactional,propagation,requires-new
// @time 30m
// @src  new
@Service
public final class TransactionalPropagationRequiresNew {

    private final AuditLogRepository auditLog;
    private final Clock clock;

    public TransactionalPropagationRequiresNew(AuditLogRepository auditLog, Clock clock) {
        this.auditLog = Objects.requireNonNull(auditLog, "auditLog");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /**
     * REQUIRES_NEW открывает ОТДЕЛЬНУЮ транзакцию: запись аудита закоммитится, даже если
     * вызывающая транзакция потом откатится. С обычным REQUIRED запись откатилась бы вместе с ней.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String action) {
        Objects.requireNonNull(action, "action");

        // ---8<--- solution
        auditLog.save(new AuditEntry(action, clock.instant()));
        // --->8--- solution
    }

    public record AuditEntry(String action, Instant occurredAt) {
    }

    public interface AuditLogRepository {
        void save(AuditEntry entry);
    }
}
