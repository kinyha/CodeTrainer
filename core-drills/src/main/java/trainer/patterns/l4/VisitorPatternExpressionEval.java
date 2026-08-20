package trainer.patterns.l4;

import java.util.Objects;

// @task patterns.l4.VisitorPatternExpressionEval
// @tags patterns,visitor,double-dispatch,ast
// @time 40m
// @src  new
public final class VisitorPatternExpressionEval {

    public interface Expr {
        <R> R accept(Visitor<R> visitor);
    }

    public record Num(double value) implements Expr {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitNum(this);
        }
    }

    public record Add(Expr left, Expr right) implements Expr {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAdd(this);
        }
    }

    public record Mul(Expr left, Expr right) implements Expr {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitMul(this);
        }
    }

    public interface Visitor<R> {
        R visitNum(Num num);

        R visitAdd(Add add);

        R visitMul(Mul mul);
    }

    private VisitorPatternExpressionEval() {
    }

    /**
     * accept()/visitX() — двойная диспетчеризация: конкретный тип узла выбирается ОБОИМИ
     * шагами (accept на узле, затем visitX на visitor'е), поэтому не нужен instanceof/switch.
     */
    public static double evaluate(Expr expr) {
        Objects.requireNonNull(expr, "expr");

        // ---8<--- solution
        return expr.accept(new Visitor<Double>() {
            @Override
            public Double visitNum(Num num) {
                return num.value();
            }

            @Override
            public Double visitAdd(Add add) {
                return add.left().accept(this) + add.right().accept(this);
            }

            @Override
            public Double visitMul(Mul mul) {
                return mul.left().accept(this) * mul.right().accept(this);
            }
        });
        // --->8--- solution
    }
}
