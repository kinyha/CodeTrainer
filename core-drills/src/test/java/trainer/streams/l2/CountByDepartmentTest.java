package trainer.streams.l2;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CountByDepartmentTest {

    @Test
    void countsEmployeesPerDepartmentSortedByName() {
        assertThat(CountByDepartment.count(List.of(
                employee("Engineering"), employee("Engineering"), employee("Marketing"))))
                .containsExactly(Map.entry("Engineering", 2L), Map.entry("Marketing", 1L));
    }

    @Test
    void emptyListGivesEmptyMap() {
        assertThat(CountByDepartment.count(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CountByDepartment.count(null));
    }

    private static Employee employee(String department) {
        return new Employee(1, "Name", department, "Dev", 50_000, LocalDate.of(2020, 1, 1), "Berlin");
    }
}
