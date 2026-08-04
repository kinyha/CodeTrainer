package trainer.patterns.l4;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// @task patterns.l4.IdempotentPaymentService
// @tags patterns,idempotency,ConcurrentHashMap,payment
// @time 40m
// @src  new
// @doc  IdempotentPaymentService.md
public final class IdempotentPaymentService {

    private final PaymentGateway gateway;
    private final ConcurrentMap<String, ProcessedPayment> processed = new ConcurrentHashMap<>();

    public IdempotentPaymentService(PaymentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway, "gateway");
    }

    /** Один paymentId списывается один раз; повтор с другим payload отклоняется. */
    public Receipt process(Payment payment) {
        Objects.requireNonNull(payment, "payment");

        // ---8<--- solution
        ProcessedPayment result = processed.compute(payment.paymentId(), (id, current) -> {
            if (current == null) {
                return new ProcessedPayment(payment, gateway.charge(payment));
            }
            if (!current.payment().equals(payment)) {
                throw new IllegalArgumentException("paymentId is already used with another payload");
            }
            return current;
        });
        return result.receipt();
        // --->8--- solution
    }

    public record Payment(String paymentId, long customerId, BigDecimal amount) {
        public Payment {
            Objects.requireNonNull(paymentId, "paymentId");
            Objects.requireNonNull(amount, "amount");
            if (paymentId.isBlank()) throw new IllegalArgumentException("paymentId must not be blank");
            if (amount.signum() <= 0) throw new IllegalArgumentException("amount must be positive");
        }
    }

    public record Receipt(String gatewayId) {
        public Receipt {
            Objects.requireNonNull(gatewayId, "gatewayId");
        }
    }

    private record ProcessedPayment(Payment payment, Receipt receipt) {
    }

    @FunctionalInterface
    public interface PaymentGateway {
        Receipt charge(Payment payment);
    }
}
