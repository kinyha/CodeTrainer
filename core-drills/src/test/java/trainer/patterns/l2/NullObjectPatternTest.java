package trainer.patterns.l2;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class NullObjectPatternTest {

    @Test
    void appliesARealDiscount() {
        NullObjectPattern.Discount tenPercentOff = subtotal -> subtotal.multiply(new BigDecimal("0.9"));
        assertThat(NullObjectPattern.checkout(new BigDecimal("100"), tenPercentOff))
                .isEqualByComparingTo("90.0");
    }

    @Test
    void noneLeavesSubtotalUnchanged() {
        assertThat(NullObjectPattern.checkout(new BigDecimal("100"), NullObjectPattern.Discount.NONE))
                .isEqualByComparingTo("100");
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> NullObjectPattern.checkout(null, NullObjectPattern.Discount.NONE));
        assertThatNullPointerException().isThrownBy(() -> NullObjectPattern.checkout(BigDecimal.TEN, null));
    }
}
