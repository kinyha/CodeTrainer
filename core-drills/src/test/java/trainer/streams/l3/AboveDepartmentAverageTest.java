package trainer.streams.l3;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Employee;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class AboveDepartmentAverageTest {

    @Test
    void keepsOnlyEmployeesAboveTheirOwnDepartmentAverage() {
        Employee low = employee("Engineering", 50_000);
        Employee high = employee("Engineering", 150_000);
        Employee soleMarketing = employee("Marketing", 70_000);

        assertThat(AboveDepartmentAverage.aboveAverage(List.of(low, high, soleMarketing)))
                .containsExactly(high);
    }

    @Test
    void emptyListGivesEmptyList() {
        assertThat(AboveDepartmentAverage.aboveAverage(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> AboveDepartmentAverage.aboveAverage(null));
    }

    private static Employee employee(String department, double salary) {
        return new Employee(1, "Name", department, "Dev", salary, LocalDate.of(2020, 1, 1), "Berlin");
    }
}
