package trainer.springdata.l3;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OptimisticLockTest {

    private final OptimisticLock.AccountRepository repository = mock();
    private final OptimisticLock service = new OptimisticLock(repository);

    @Test
    void updatesBalanceWhenVersionMatches() {
        when(repository.findById(1)).thenReturn(Optional.of(new OptimisticLock.Account(1, 100, 3)));
        when(repository.updateIfVersionMatches(1, 150, 3)).thenReturn(true);

        service.deposit(1, 50);

        verify(repository).updateIfVersionMatches(1, 150, 3);
    }

    @Test
    void throwsWhenAnotherWriteWonTheRace() {
        when(repository.findById(1)).thenReturn(Optional.of(new OptimisticLock.Account(1, 100, 3)));
        when(repository.updateIfVersionMatches(1, 150, 3)).thenReturn(false);

        assertThatExceptionOfType(OptimisticLock.OptimisticLockException.class)
                .isThrownBy(() -> service.deposit(1, 50));
    }

    @Test
    void rejectsUnknownAccount() {
        when(repository.findById(9)).thenReturn(Optional.empty());
        assertThatIllegalArgumentException().isThrownBy(() -> service.deposit(9, 50));
    }
}
