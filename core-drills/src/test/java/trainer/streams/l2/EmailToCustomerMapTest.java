package trainer.streams.l2;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Customer;
import trainer.fixtures.sales.CustomerType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmailToCustomerMapTest {

    @Test
    void resolvesDuplicateKeysWithLatestRegistration() {
        Customer old = customer(1, "same@mail.test", LocalDate.of(2020, 1, 1));
        Customer other = customer(2, "other@mail.test", LocalDate.of(2022, 1, 1));
        Customer latest = customer(3, "same@mail.test", LocalDate.of(2024, 1, 1));

        assertThat(EmailToCustomerMap.index(List.of(old, other, latest)))
                .containsExactly(
                        org.assertj.core.api.Assertions.entry("same@mail.test", latest),
                        org.assertj.core.api.Assertions.entry("other@mail.test", other));
    }

    private static Customer customer(long id, String email, LocalDate registered) {
        return new Customer(id, "Customer " + id, email, registered, "Minsk", CustomerType.REGULAR, true);
    }
}
