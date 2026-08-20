package trainer.patterns.l4;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

// @task patterns.l4.ChainOfResponsibilityValidation
// @tags patterns,chain-of-responsibility,validation
// @time 40m
// @src  new
public final class ChainOfResponsibilityValidation {

    @FunctionalInterface
    public interface Validator {
        Optional<String> validate(String input);
    }

    private ChainOfResponsibilityValidation() {
    }

    /**
     * Каждый валидатор в цепочке ничего не знает о соседях: run() останавливается на
     * ПЕРВОЙ найденной ошибке и не запускает остальные — так же, как servlet filter chain.
     */
    public static Optional<String> run(String input, List<Validator> chain) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(chain, "chain");

        // ---8<--- solution
        for (Validator validator : chain) {
            Optional<String> error = validator.validate(input);
            if (error.isPresent()) {
                return error;
            }
        }
        return Optional.empty();
        // --->8--- solution
    }
}
