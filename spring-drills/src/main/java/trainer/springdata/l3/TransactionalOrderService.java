package trainer.springdata.l3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

// @task springdata.l3.TransactionalOrderService
// @tags spring-data,transactional,service,boundary
// @time 25m
// @src  new
// @doc  TransactionalOrderService.md
@Service
public final class TransactionalOrderService {

    private final OrderRepository repository;

    public TransactionalOrderService(OrderRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    /** Проверка и изменение выполняются в одной транзакционной границе. */
    @Transactional
    public void cancel(long orderId) {
        // ---8<--- solution
        if (!repository.existsById(orderId)) {
            throw new OrderNotFoundException(orderId);
        }
        repository.setStatus(orderId, "CANCELLED");
        // --->8--- solution
    }

    public interface OrderRepository {
        boolean existsById(long id);

        void setStatus(long id, String status);
    }

    public static final class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(long id) {
            super("Order %d was not found".formatted(id));
        }
    }
}
