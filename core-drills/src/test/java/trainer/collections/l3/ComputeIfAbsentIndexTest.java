package trainer.collections.l3;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Employee;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ComputeIfAbsentIndexTest {

    @Test
    void indexesEmployeesAndFreezesNestedLists() {
        Employee ana = employee(1, "Ana", "Platform");
        Employee max = employee(2, "Max", "Sales");
        Employee lee = employee(3, "Lee", "Platform");

        var index = ComputeIfAbsentIndex.byDepartment(List.of(ana, max, lee));

        assertThat(index.get("Platform")).containsExactly(ana, lee);
        assertThat(index.get("Sales")).containsExactly(max);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> index.get("Platform").add(max));
    }

    private static Employee employee(long id, String name, String department) {
        return new Employee(id, name, department, "Engineer", 100, LocalDate.of(2020, 1, 1), "Minsk");
    }
}
