package trainer.streams.l1;

import trainer.fixtures.sales.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// @task streams.l1.MaxSalary
// @tags streams,max,Optional,Comparator
// @time 6m
// @src  new
public final class MaxSalary {

    private MaxSalary() {
    }

    /** Пустой список -> Optional.empty(), а не исключение. */
    public static Optional<Employee> highestPaid(List<Employee> employees) {
        Objects.requireNonNull(employees, "employees");

        // ---8<--- solution
        return employees.stream()
                .peek(employee -> Objects.requireNonNull(employee, "employee"))
                .max(Comparator.comparingDouble(Employee::salary));
        // --->8--- solution
    }
}
