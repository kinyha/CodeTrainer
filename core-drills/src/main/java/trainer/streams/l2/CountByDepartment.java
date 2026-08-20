package trainer.streams.l2;

import trainer.fixtures.sales.Employee;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

// @task streams.l2.CountByDepartment
// @tags streams,groupingBy,counting,TreeMap
// @time 10m
// @src  new
public final class CountByDepartment {

    private CountByDepartment() {
    }

    public static Map<String, Long> count(List<Employee> employees) {
        Objects.requireNonNull(employees, "employees");

        // ---8<--- solution
        return employees.stream()
                .peek(employee -> Objects.requireNonNull(employee, "employee"))
                .collect(Collectors.groupingBy(Employee::department, TreeMap::new, Collectors.counting()));
        // --->8--- solution
    }
}
