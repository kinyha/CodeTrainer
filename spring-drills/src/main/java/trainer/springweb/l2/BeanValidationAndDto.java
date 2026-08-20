package trainer.springweb.l2;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Objects;
import java.util.Set;

// @task springweb.l2.BeanValidationAndDto
// @tags spring-mvc,bean-validation,dto,constraints
// @time 12m
// @src  new
public final class BeanValidationAndDto {

    public record CreateProduct(
            @NotBlank String name,
            @Min(0) int stock
    ) {
    }

    private final Validator validator;

    public BeanValidationAndDto(Validator validator) {
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    /** Валидируем явно (без @Valid в контроллере) — например, для DTO, приходящих не из HTTP. */
    public List<String> violationMessages(CreateProduct request) {
        Objects.requireNonNull(request, "request");

        // ---8<--- solution
        Set<ConstraintViolation<CreateProduct>> violations = validator.validate(request);
        return violations.stream().map(ConstraintViolation::getMessage).sorted().toList();
        // --->8--- solution
    }
}
