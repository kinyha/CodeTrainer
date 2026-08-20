package trainer.streams.l3;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Customer;
import trainer.fixtures.sales.CustomerType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DistinctByKeyTest {

    @Test
    void keepsOnlyTheFirstCustomerPerCity() {
        Customer berlinFirst = customer("Anna", "Berlin");
        Customer berlinSecond = customer("Ben", "Berlin");
        Customer paris = customer("Chloe", "Paris");

        assertThat(DistinctByKey.distinctByCity(List.of(berlinFirst, berlinSecond, paris)))
                .containsExactly(berlinFirst, paris);
    }

    @Test
    void emptyListGivesEmptyList() {
        assertThat(DistinctByKey.distinctByCity(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> DistinctByKey.distinctByCity(null));
    }

    private static Customer customer(String name, String city) {
        return new Customer(1, name, name.toLowerCase() + "@example.com", LocalDate.of(2020, 1, 1), city, CustomerType.REGULAR, true);
    }
}
