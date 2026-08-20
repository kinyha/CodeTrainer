package trainer.streams.l3;

import trainer.fixtures.sales.Customer;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

// @task streams.l3.DistinctByKey
// @tags streams,distinct,stateful-predicate,gotcha
// @time 25m
// @src  new
public final class DistinctByKey {

    private DistinctByKey() {
    }

    /**
     * У Stream нет distinct(keyExtractor) — только distinct() по equals(). Обходной путь:
     * stateful-предикат, который помнит уже виденные ключи. Оставляет первое вхождение ключа.
     */
    public static List<Customer> distinctByCity(List<Customer> customers) {
        Objects.requireNonNull(customers, "customers");

        // ---8<--- solution
        Set<String> seenCities = ConcurrentHashMap.newKeySet();
        Predicate<Customer> isFirstForCity = customer -> seenCities.add(customer.city());
        return customers.stream()
                .peek(customer -> Objects.requireNonNull(customer, "customer"))
                .filter(isFirstForCity)
                .toList();
        // --->8--- solution
    }
}
