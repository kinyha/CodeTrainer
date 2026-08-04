package trainer.patterns.l4;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

// @task patterns.l4.TransferInvariantReview
// @tags code-review,BigDecimal,invariants,defensive-copy,atomicity
// @time 45m
// @src  new
// @doc  TransferInvariantReview.md
public final class TransferInvariantReview {

    private TransferInvariantReview() {
    }

    /** Возвращает новый snapshot; при любой ошибке исходные balances не меняются. */
    public static Map<Long, BigDecimal> transfer(
            Map<Long, BigDecimal> balances,
            long from,
            long to,
            BigDecimal amount
    ) {
        Objects.requireNonNull(balances, "balances");
        Objects.requireNonNull(amount, "amount");

        // ---8<--- solution
        if (from == to) throw new IllegalArgumentException("accounts must differ");
        if (amount.signum() <= 0) throw new IllegalArgumentException("amount must be positive");
        BigDecimal source = Objects.requireNonNull(balances.get(from), "source account");
        BigDecimal target = Objects.requireNonNull(balances.get(to), "target account");
        if (source.compareTo(amount) < 0) throw new IllegalArgumentException("insufficient funds");

        Map<Long, BigDecimal> result = new LinkedHashMap<>(balances);
        result.put(from, source.subtract(amount));
        result.put(to, target.add(amount));
        return Collections.unmodifiableMap(result);
        // --->8--- solution
    }
}
