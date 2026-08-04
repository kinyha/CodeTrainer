package trainer.springdata.l3;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionalOrderServiceTest {

    private final TransactionalOrderService.OrderRepository repository = mock();
    private final TransactionalOrderService service = new TransactionalOrderService(repository);

    @Test
    void marksExistingOrderAsCancelled() {
        when(repository.existsById(7)).thenReturn(true);
        service.cancel(7);
        verify(repository).setStatus(7, "CANCELLED");
    }

    @Test
    void doesNotWriteMissingOrder() {
        assertThatExceptionOfType(TransactionalOrderService.OrderNotFoundException.class)
                .isThrownBy(() -> service.cancel(9));
        verify(repository, never()).setStatus(9, "CANCELLED");
    }

    @Test
    void declaresTransactionAtPublicBoundary() throws Exception {
        var method = TransactionalOrderService.class.getMethod("cancel", long.class);
        assertThat(method.getAnnotation(Transactional.class)).isNotNull();
    }
}
