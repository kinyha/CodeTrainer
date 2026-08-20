package trainer.patterns.l2;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class StrategyEnumTest {

    @Test
    void calculatesStandardFee() {
        assertThat(StrategyEnum.fee(StrategyEnum.ShippingFee.STANDARD, new BigDecimal("100.00")))
                .isEqualByComparingTo("5.00");
    }

    @Test
    void calculatesExpressFee() {
        assertThat(StrategyEnum.fee(StrategyEnum.ShippingFee.EXPRESS, new BigDecimal("100.00")))
                .isEqualByComparingTo("15.00");
    }

    @Test
    void freeShippingHasNoFee() {
        assertThat(StrategyEnum.fee(StrategyEnum.ShippingFee.FREE, new BigDecimal("100.00")))
                .isEqualByComparingTo("0.00");
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> StrategyEnum.fee(null, BigDecimal.TEN));
        assertThatNullPointerException().isThrownBy(() -> StrategyEnum.fee(StrategyEnum.ShippingFee.FREE, null));
    }
}
