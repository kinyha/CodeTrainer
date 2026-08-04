package trainer.patterns.l3;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DiscountPolicyChainTest {

    @Test
    void appliesFirstMatchingPolicyAndRoundsMoney() {
        var order = new DiscountPolicyChain.Order(7, new BigDecimal("100.00"), true);
        DiscountPolicyChain.DiscountPolicy premium = value -> value.premium()
                ? Optional.of(new BigDecimal("12.345")) : Optional.empty();
        DiscountPolicyChain.DiscountPolicy fallback = value -> Optional.of(new BigDecimal("5.00"));

        assertThat(DiscountPolicyChain.quote(order, List.of(premium, fallback)))
                .isEqualTo(new DiscountPolicyChain.Quote(
                        new BigDecimal("100.00"), new BigDecimal("12.35"), new BigDecimal("87.65")));
    }

    @Test
    void rejectsDiscountLargerThanSubtotal() {
        var order = new DiscountPolicyChain.Order(7, BigDecimal.TEN, false);
        assertThatIllegalArgumentException().isThrownBy(() ->
                DiscountPolicyChain.quote(order, List.of(value -> Optional.of(new BigDecimal("11.00")))));
    }
}
