package trainer.streams.l2;

import trainer.fixtures.sales.Customer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// @task streams.l2.PartitionByActive
// @tags streams,partitioningBy,boolean
// @time 10m
// @src  new
public final class PartitionByActive {

    private PartitionByActive() {
    }

    /** Ключ true — активные клиенты, false — неактивные. Обе группы есть всегда, даже пустые. */
    public static Map<Boolean, List<Customer>> partition(List<Customer> customers) {
        Objects.requireNonNull(customers, "customers");

        // ---8<--- solution
        return customers.stream()
                .peek(customer -> Objects.requireNonNull(customer, "customer"))
                .collect(Collectors.partitioningBy(Customer::active));
        // --->8--- solution
    }
}
