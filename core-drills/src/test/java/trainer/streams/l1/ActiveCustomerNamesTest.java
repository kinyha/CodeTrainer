package trainer.streams.l1;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Customer;
import trainer.fixtures.sales.CustomerType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveCustomerNamesTest {

    @Test
    void filtersDeduplicatesAndSortsNames() {
        List<Customer> customers = List.of(
                customer(1, "zoe", true), customer(2, "Ana", false),
                customer(3, "Bob", true), customer(4, "zoe", true));

        assertThat(ActiveCustomerNames.collect(customers)).containsExactly("Bob", "zoe");
    }

    private static Customer customer(long id, String name, boolean active) {
        return new Customer(id, name, name + "@mail.test", LocalDate.of(2024, 1, 1),
                "Minsk", CustomerType.REGULAR, active);
    }
}
