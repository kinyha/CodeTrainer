package trainer.collections.l2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ComparatorChainTest {

    @Test
    void appliesEveryTieBreakerAndLeavesInputUntouched() {
        var bobLow = new ComparatorChain.Applicant(3, "Bob", 70);
        var nullName = new ComparatorChain.Applicant(4, null, 100);
        var aliceLate = new ComparatorChain.Applicant(2, "alice", 90);
        var aliceFirst = new ComparatorChain.Applicant(1, "Alice", 90);
        var bobHigh = new ComparatorChain.Applicant(5, "bob", 95);
        List<ComparatorChain.Applicant> input = List.of(bobLow, nullName, aliceLate, aliceFirst, bobHigh);

        assertThat(ComparatorChain.sort(input))
                .containsExactly(aliceFirst, aliceLate, bobHigh, bobLow, nullName);
        assertThat(input).startsWith(bobLow);
    }
}
