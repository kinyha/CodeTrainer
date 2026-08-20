package trainer.springdata.l2;

import java.util.List;
import java.util.Objects;

// @task springdata.l2.ProjectionsInterfaceAndDto
// @tags spring-data,projection,dto,over-fetching
// @time 12m
// @src  new
public final class ProjectionsInterfaceAndDto {

    private ProjectionsInterfaceAndDto() {
    }

    public record Customer(long id, String name, String email, String internalNotes) {
    }

    /** Projection — не весь Customer, только то, что нужно списку: internalNotes не тянем из БД зря. */
    public record CustomerSummary(long id, String name) {
    }

    public static List<CustomerSummary> toSummaries(List<Customer> customers) {
        Objects.requireNonNull(customers, "customers");

        // ---8<--- solution
        return customers.stream()
                .map(customer -> new CustomerSummary(customer.id(), customer.name()))
                .toList();
        // --->8--- solution
    }
}
