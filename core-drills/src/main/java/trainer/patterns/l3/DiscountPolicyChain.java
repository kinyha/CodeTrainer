package trainer.patterns.l3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// @task patterns.l3.DiscountPolicyChain
// @tags patterns,chain-of-responsibility,BigDecimal,business-rule
// @time 25m
// @src  new
public final class DiscountPolicyChain {

    private DiscountPolicyChain() {
    }

    /** Применяет первую подходящую политику; порядок списка задаёт приоритет. */
    public static Quote quote(Order order, List<DiscountPolicy> policies) {
        Objects.requireNonNull(order, "order");
        Objects.requireNonNull(policies, "policies");

        // ---8<--- solution
        BigDecimal discount = policies.stream()
                .peek(policy -> Objects.requireNonNull(policy, "policy"))
                .map(policy -> policy.discountFor(order))
                .flatMap(Optional::stream)
                .findFirst()
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        if (discount.signum() < 0 || discount.compareTo(order.subtotal()) > 0) {
            throw new IllegalArgumentException("invalid discount");
        }
        return new Quote(order.subtotal(), discount, order.subtotal().subtract(discount));
        // --->8--- solution
    }

    public record Order(long customerId, BigDecimal subtotal, boolean premium) {
        public Order {
            Objects.requireNonNull(subtotal, "subtotal");
            if (subtotal.signum() < 0) throw new IllegalArgumentException("subtotal must be non-negative");
        }
    }

    public record Quote(BigDecimal subtotal, BigDecimal discount, BigDecimal total) {
    }

    @FunctionalInterface
    public interface DiscountPolicy {
        Optional<BigDecimal> discountFor(Order order);
    }
}
