package trainer.streams.l1;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MaxSalaryTest {

    @Test
    void findsTheHighestPaidEmployee() {
        Employee low = employee(1, "Low", 50_000);
        Employee high = employee(2, "High", 90_000);
        Employee mid = employee(3, "Mid", 70_000);

        assertThat(MaxSalary.highestPaid(List.of(low, high, mid))).contains(high);
    }

    @Test
    void emptyListGivesEmptyOptional() {
        assertThat(MaxSalary.highestPaid(List.of())).isEqualTo(Optional.empty());
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> MaxSalary.highestPaid(null));
    }

    private static Employee employee(long id, String name, double salary) {
        return new Employee(id, name, "Engineering", "Dev", salary, LocalDate.of(2020, 1, 1), "Berlin");
    }
}
