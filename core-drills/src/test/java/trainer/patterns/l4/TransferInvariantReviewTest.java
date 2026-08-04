package trainer.patterns.l4;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.entry;

class TransferInvariantReviewTest {

    @Test
    void transfersMoneyWithoutMutatingSourceAndPreservesTotal() {
        Map<Long, BigDecimal> source = new LinkedHashMap<>();
        source.put(1L, new BigDecimal("100.00"));
        source.put(2L, new BigDecimal("20.00"));

        var result = TransferInvariantReview.transfer(source, 1, 2, new BigDecimal("30.00"));

        assertThat(result).containsExactly(entry(1L, new BigDecimal("70.00")), entry(2L, new BigDecimal("50.00")));
        assertThat(source).containsEntry(1L, new BigDecimal("100.00"));
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> result.put(3L, BigDecimal.ZERO));
    }

    @Test
    void rejectsInsufficientFundsBeforeCreatingPartialState() {
        Map<Long, BigDecimal> source = Map.of(1L, BigDecimal.ONE, 2L, BigDecimal.TEN);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> TransferInvariantReview.transfer(source, 1, 2, BigDecimal.TEN))
                .withMessage("insufficient funds");
        assertThat(source.get(1L)).isEqualByComparingTo(BigDecimal.ONE);
    }
}
