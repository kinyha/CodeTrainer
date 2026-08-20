package trainer.springdata.l3;

import java.util.Objects;
import java.util.Optional;

// @task springdata.l3.OptimisticLock
// @tags spring-data,optimistic-locking,version,concurrent-modification
// @time 25m
// @src  new
public final class OptimisticLock {

    public record Account(long id, long balance, int version) {
    }

    public interface AccountRepository {
        Optional<Account> findById(long id);

        /** В реальном @Version-поле Hibernate делает ровно это сам через WHERE id=? AND version=?. */
        boolean updateIfVersionMatches(long id, long newBalance, int expectedVersion);
    }

    private final AccountRepository repository;

    public OptimisticLock(AccountRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    /**
     * @Version не даёт молча перезаписать чужое изменение: условие в WHERE проверяет версию,
     * а несовпадение — сигнал, что кто-то другой уже обновил запись между чтением и записью.
     */
    public void deposit(long accountId, long amount) {
        // ---8<--- solution
        Account account = repository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account not found: " + accountId));
        boolean updated = repository.updateIfVersionMatches(
                accountId, account.balance() + amount, account.version());
        if (!updated) {
            throw new OptimisticLockException(accountId);
        }
        // --->8--- solution
    }

    public static final class OptimisticLockException extends RuntimeException {
        public OptimisticLockException(long accountId) {
            super("account %d was modified concurrently".formatted(accountId));
        }
    }
}
