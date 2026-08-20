package trainer.streams.l3;

import trainer.fixtures.sales.Employee;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// @task streams.l3.AboveDepartmentAverage
// @tags streams,groupingBy,averagingDouble,two-pass
// @time 28m
// @src  new
public final class AboveDepartmentAverage {

    private AboveDepartmentAverage() {
    }

    /** Сотрудники, чья зарплата выше средней по ИХ отделу. Нужны два прохода: сначала считаем средние. */
    public static List<Employee> aboveAverage(List<Employee> employees) {
        Objects.requireNonNull(employees, "employees");

        // ---8<--- solution
        Map<String, Double> averageByDepartment = employees.stream()
                .peek(employee -> Objects.requireNonNull(employee, "employee"))
                .collect(Collectors.groupingBy(Employee::department, Collectors.averagingDouble(Employee::salary)));

        return employees.stream()
                .filter(employee -> employee.salary() > averageByDepartment.get(employee.department()))
                .toList();
        // --->8--- solution
    }
}
