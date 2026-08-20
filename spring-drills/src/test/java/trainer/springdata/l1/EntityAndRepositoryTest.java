package trainer.springdata.l1;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntityAndRepositoryTest {

    private final EntityAndRepository.CustomerRepository repository = mock();
    private final EntityAndRepository service = new EntityAndRepository(repository);

    @Test
    void returnsNameWhenCustomerExists() {
        when(repository.findById(1)).thenReturn(Optional.of(new EntityAndRepository.Customer(1, "Ada")));
        assertThat(service.displayName(1)).isEqualTo("Ada");
    }

    @Test
    void returnsFallbackWhenCustomerIsMissing() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        assertThat(service.displayName(2)).isEqualTo("Unknown customer");
    }
}
