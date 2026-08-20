package trainer.streams.l2;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Customer;
import trainer.fixtures.sales.CustomerType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PartitionByActiveTest {

    @Test
    void splitsActiveAndInactiveCustomers() {
        Customer active = customer(true);
        Customer inactive = customer(false);

        Map<Boolean, List<Customer>> result = PartitionByActive.partition(List.of(active, inactive));

        assertThat(result.get(true)).containsExactly(active);
        assertThat(result.get(false)).containsExactly(inactive);
    }

    @Test
    void bothKeysArePresentEvenWhenOneGroupIsEmpty() {
        Map<Boolean, List<Customer>> result = PartitionByActive.partition(List.of(customer(true)));

        assertThat(result).containsKey(false);
        assertThat(result.get(false)).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> PartitionByActive.partition(null));
    }

    private static Customer customer(boolean active) {
        return new Customer(1, "Name", "name@example.com", LocalDate.of(2020, 1, 1), "Berlin", CustomerType.REGULAR, active);
    }
}
