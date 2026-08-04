package trainer.streams.l2;

import trainer.fixtures.sales.Customer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

// @task streams.l2.EmailToCustomerMap
// @tags streams,toMap,merge-function,LinkedHashMap
// @time 15m
// @src  new
public final class EmailToCustomerMap {

    private EmailToCustomerMap() {
    }

    /** При дубликате email оставляет клиента с более поздней регистрацией. */
    public static Map<String, Customer> index(List<Customer> customers) {
        Objects.requireNonNull(customers, "customers");

        // ---8<--- solution
        return customers.stream()
                .peek(customer -> Objects.requireNonNull(customer, "customer"))
                .collect(Collectors.toMap(
                        Customer::email,
                        Function.identity(),
                        (left, right) -> left.registrationDate().isAfter(right.registrationDate()) ? left : right,
                        LinkedHashMap::new
                ));
        // --->8--- solution
    }
}
