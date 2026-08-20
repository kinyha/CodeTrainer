package trainer.streams.l1;

import trainer.fixtures.sales.Customer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// @task streams.l1.JoinCustomerNames
// @tags streams,joining,sorted,String
// @time 6m
// @src  new
public final class JoinCustomerNames {

    private JoinCustomerNames() {
    }

    /** Имена по алфавиту через ", ". Пустой список даёт пустую строку. */
    public static String join(List<Customer> customers) {
        Objects.requireNonNull(customers, "customers");

        // ---8<--- solution
        return customers.stream()
                .peek(customer -> Objects.requireNonNull(customer, "customer"))
                .map(Customer::name)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(", "));
        // --->8--- solution
    }
}
