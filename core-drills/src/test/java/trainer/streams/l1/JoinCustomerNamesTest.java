package trainer.streams.l1;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Customer;
import trainer.fixtures.sales.CustomerType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class JoinCustomerNamesTest {

    @Test
    void joinsNamesInAlphabeticalOrder() {
        assertThat(JoinCustomerNames.join(List.of(customer("Charlie"), customer("Alice"), customer("Bob"))))
                .isEqualTo("Alice, Bob, Charlie");
    }

    @Test
    void singleCustomerHasNoDelimiter() {
        assertThat(JoinCustomerNames.join(List.of(customer("Solo")))).isEqualTo("Solo");
    }

    @Test
    void emptyListGivesEmptyString() {
        assertThat(JoinCustomerNames.join(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> JoinCustomerNames.join(null));
    }

    private static Customer customer(String name) {
        return new Customer(1, name, name.toLowerCase() + "@example.com", LocalDate.of(2020, 1, 1), "Berlin", CustomerType.REGULAR, true);
    }
}
