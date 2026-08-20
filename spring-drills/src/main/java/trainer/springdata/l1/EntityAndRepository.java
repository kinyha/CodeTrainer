package trainer.springdata.l1;

import java.util.Objects;
import java.util.Optional;

// @task springdata.l1.EntityAndRepository
// @tags spring-data,repository,entity,optional
// @time 8m
// @src  new
public final class EntityAndRepository {

    private final CustomerRepository repository;

    public EntityAndRepository(CustomerRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    /** Repository возвращает Optional — искать через findById и не бросать исключение самостоятельно. */
    public String displayName(long customerId) {
        // ---8<--- solution
        return repository.findById(customerId)
                .map(Customer::name)
                .orElse("Unknown customer");
        // --->8--- solution
    }

    public record Customer(long id, String name) {
    }

    public interface CustomerRepository {
        Optional<Customer> findById(long id);
    }
}
