package trainer.patterns.l3;

import java.util.Objects;

// @task patterns.l3.SpecificationCompositePredicate
// @tags patterns,specification,composite,predicate
// @time 25m
// @src  new
public final class SpecificationCompositePredicate {

    private SpecificationCompositePredicate() {
    }

    @FunctionalInterface
    public interface Specification<T> {
        boolean isSatisfiedBy(T candidate);

        /** and() возвращает НОВУЮ спецификацию — исходные две остаются неизменными и переиспользуемыми. */
        default Specification<T> and(Specification<T> other) {
            Objects.requireNonNull(other, "other");

            // ---8<--- solution
            return candidate -> this.isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
            // --->8--- solution
        }

        default Specification<T> negate() {
            return candidate -> !this.isSatisfiedBy(candidate);
        }
    }
}
