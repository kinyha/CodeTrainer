package trainer.springcore.l2;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindCustomerServiceTest {

    private final FindCustomerService.CustomerRepository repository = mock();
    private final FindCustomerService service = new FindCustomerService(repository);

    @Test
    void mapsEntityToView() {
        when(repository.findById(7)).thenReturn(Optional.of(new FindCustomerService.Customer(7, "Ada")));

        assertThat(service.find(7)).isEqualTo(new FindCustomerService.CustomerView(7, "Ada"));
        verify(repository).findById(7);
    }

    @Test
    void throwsDomainErrorForMissingCustomer() {
        when(repository.findById(9)).thenReturn(Optional.empty());
        assertThatExceptionOfType(FindCustomerService.CustomerNotFoundException.class)
                .isThrownBy(() -> service.find(9))
                .withMessage("Customer 9 was not found");
    }
}
