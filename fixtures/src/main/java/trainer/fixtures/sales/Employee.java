package trainer.fixtures.sales;

import java.time.LocalDate;
import java.util.Objects;

public record Employee(
        long id,
        String name,
        String department,
        String position,
        double salary,
        LocalDate hireDate,
        String city
) {
    public Employee {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(department, "department");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(hireDate, "hireDate");
        Objects.requireNonNull(city, "city");
        if (!Double.isFinite(salary) || salary < 0) {
            throw new IllegalArgumentException("salary must be finite and non-negative");
        }
    }
}
