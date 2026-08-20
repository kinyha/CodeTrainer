package trainer.patterns.l4;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.within;

class VisitorPatternExpressionEvalTest {

    @Test
    void evaluatesANestedExpression() {
        // (2 + 3) * 4
        VisitorPatternExpressionEval.Expr expr = new VisitorPatternExpressionEval.Mul(
                new VisitorPatternExpressionEval.Add(
                        new VisitorPatternExpressionEval.Num(2),
                        new VisitorPatternExpressionEval.Num(3)),
                new VisitorPatternExpressionEval.Num(4));

        assertThat(VisitorPatternExpressionEval.evaluate(expr)).isCloseTo(20.0, within(0.0001));
    }

    @Test
    void evaluatesABareNumber() {
        assertThat(VisitorPatternExpressionEval.evaluate(new VisitorPatternExpressionEval.Num(7)))
                .isCloseTo(7.0, within(0.0001));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> VisitorPatternExpressionEval.evaluate(null));
    }
}
