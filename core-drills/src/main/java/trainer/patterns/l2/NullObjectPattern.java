package trainer.patterns.l2;

import java.math.BigDecimal;
import java.util.Objects;

// @task patterns.l2.NullObjectPattern
// @tags patterns,null-object,polymorphism
// @time 12m
// @src  new
public final class NullObjectPattern {

    private NullObjectPattern() {
    }

    public interface Discount {
        Discount NONE = subtotal -> subtotal;

        BigDecimal apply(BigDecimal subtotal);
    }

    /** Вызывающему коду не нужен null-check: NONE ведёт себя как реальная стратегия без эффекта. */
    public static BigDecimal checkout(BigDecimal subtotal, Discount discount) {
        Objects.requireNonNull(subtotal, "subtotal");
        Objects.requireNonNull(discount, "discount");

        // ---8<--- solution
        return discount.apply(subtotal);
        // --->8--- solution
    }
}
