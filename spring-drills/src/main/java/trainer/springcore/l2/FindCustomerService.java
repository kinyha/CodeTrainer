package trainer.springcore.l2;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

// @task springcore.l2.FindCustomerService
// @tags spring-core,constructor-injection,service,optional
// @time 15m
// @src  new
@Service
public final class FindCustomerService {

    private final CustomerRepository repository;

    public FindCustomerService(CustomerRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    /** Возвращает проекцию клиента либо доменную ошибку. */
    public CustomerView find(long customerId) {
        // ---8<--- solution
        return repository.findById(customerId)
                .map(customer -> new CustomerView(customer.id(), customer.name()))
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        // --->8--- solution
    }

    public interface CustomerRepository {
        Optional<Customer> findById(long id);
    }

    public record Customer(long id, String name) {
    }

    public record CustomerView(long id, String name) {
    }

    public static final class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(long id) {
            super("Customer %d was not found".formatted(id));
        }
    }
}
