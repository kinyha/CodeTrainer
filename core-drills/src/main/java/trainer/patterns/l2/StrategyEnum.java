package trainer.patterns.l2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

// @task patterns.l2.StrategyEnum
// @tags patterns,strategy,enum,polymorphism
// @time 15m
// @src  new
public final class StrategyEnum {

    private StrategyEnum() {
    }

    /** Каждая константа enum — своя реализация стратегии; никакого if/switch в вызывающем коде. */
    public enum ShippingFee {
        STANDARD {
            @Override
            public BigDecimal feeFor(BigDecimal subtotal) {
                // ---8<--- solution
                return subtotal.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
                // --->8--- solution
            }
        },
        EXPRESS {
            @Override
            public BigDecimal feeFor(BigDecimal subtotal) {
                // ---8<--- solution
                return subtotal.multiply(BigDecimal.valueOf(0.15)).setScale(2, RoundingMode.HALF_UP);
                // --->8--- solution
            }
        },
        FREE {
            @Override
            public BigDecimal feeFor(BigDecimal subtotal) {
                // ---8<--- solution
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                // --->8--- solution
            }
        };

        public abstract BigDecimal feeFor(BigDecimal subtotal);
    }

    public static BigDecimal fee(ShippingFee strategy, BigDecimal subtotal) {
        Objects.requireNonNull(strategy, "strategy");
        Objects.requireNonNull(subtotal, "subtotal");
        return strategy.feeFor(subtotal);
    }
}
