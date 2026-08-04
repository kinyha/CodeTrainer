package trainer.streams.l1;

import trainer.fixtures.sales.Customer;

import java.util.List;
import java.util.Objects;

// @task streams.l1.ActiveCustomerNames
// @tags streams,filter,map,sorted
// @time 8m
// @src  new
public final class ActiveCustomerNames {

    private ActiveCustomerNames() {
    }

    /** Возвращает уникальные имена активных клиентов в алфавитном порядке. */
    public static List<String> collect(List<Customer> customers) {
        Objects.requireNonNull(customers, "customers");

        // ---8<--- solution
        return customers.stream()
                .peek(customer -> Objects.requireNonNull(customer, "customer"))
                .filter(Customer::active)
                .map(Customer::name)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        // --->8--- solution
    }
}
