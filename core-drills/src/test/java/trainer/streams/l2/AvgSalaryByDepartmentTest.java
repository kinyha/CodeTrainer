package trainer.streams.l2;

import org.junit.jupiter.api.Test;
import trainer.fixtures.sales.Employee;
import trainer.fixtures.sales.SalesData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class AvgSalaryByDepartmentTest {

    @Test
    void calculatesAveragesAndSortsDepartmentNames() {
        List<Employee> reversed = new ArrayList<>(SalesData.employees());
        reversed.sort((left, right) -> Long.compare(right.id(), left.id()));

        Map<String, Double> result = AvgSalaryByDepartment.calculate(reversed);

        assertThat(result.keySet())
                .containsExactly("Engineering", "Marketing", "Operations");
        assertThat(result)
                .containsEntry("Engineering", 150_000.0)
                .containsEntry("Marketing", 100_000.0)
                .containsEntry("Operations", 120_000.0);
    }

    @Test
    void returnsEmptyMapForEmptyInput() {
        assertThat(AvgSalaryByDepartment.calculate(List.of())).isEmpty();
    }

    @Test
    void rejectsNullInput() {
        assertThatNullPointerException()
                .isThrownBy(() -> AvgSalaryByDepartment.calculate(null))
                .withMessage("employees");
    }
}
