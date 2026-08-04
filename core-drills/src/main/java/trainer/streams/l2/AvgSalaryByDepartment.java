package trainer.streams.l2;

import trainer.fixtures.sales.Employee;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

// @task streams.l2.AvgSalaryByDepartment
// @tags groupingBy,averagingDouble,TreeMap,mapFactory
// @time 15m
// @src  LeetCode-75-Study-Project:streamExercise/Tasks_v1.java#2.2
// @doc  AvgSalaryByDepartment.md
public final class AvgSalaryByDepartment {

    private AvgSalaryByDepartment() {
    }

    /**
     * Возвращает среднюю зарплату по отделам с ключами в алфавитном порядке.
     * Контракт: пустой список даёт пустую Map; null вместо списка запрещён.
     * Готово, когда порядок ключей не зависит от порядка сотрудников на входе.
     */
    public static Map<String, Double> calculate(List<Employee> employees) {
        Objects.requireNonNull(employees, "employees");

        // ---8<--- solution
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        TreeMap::new, // WHY: трёхаргументная форма позволяет сразу выбрать сортированную Map
                        Collectors.averagingDouble(Employee::salary)
                ));
        // --->8--- solution
    }
}
