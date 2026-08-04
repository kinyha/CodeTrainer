package trainer.collections.l3;

import trainer.fixtures.sales.Employee;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @task collections.l3.ComputeIfAbsentIndex
// @tags Map,computeIfAbsent,index,immutable
// @time 20m
// @src  new
// @doc  ComputeIfAbsentIndex.md
public final class ComputeIfAbsentIndex {

    private ComputeIfAbsentIndex() {
    }

    /** Индексирует сотрудников по отделу с сохранением порядка входа. */
    public static Map<String, List<Employee>> byDepartment(List<Employee> employees) {
        Objects.requireNonNull(employees, "employees");

        // ---8<--- solution
        Map<String, List<Employee>> mutable = new LinkedHashMap<>();
        for (Employee employee : employees) {
            Objects.requireNonNull(employee, "employee");
            mutable.computeIfAbsent(employee.department(), ignored -> new ArrayList<>()).add(employee);
        }
        Map<String, List<Employee>> result = new LinkedHashMap<>();
        mutable.forEach((department, members) -> result.put(department, List.copyOf(members)));
        return Map.copyOf(result);
        // --->8--- solution
    }
}
