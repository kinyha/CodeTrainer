package trainer.springcore.l2;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.Set;

// @task springcore.l2.ConfigurationPropertiesValidation
// @tags spring-core,configuration-properties,validation,type-safe-config
// @time 15m
// @src  new
public final class ConfigurationPropertiesValidation {

    @ConfigurationProperties(prefix = "mail")
    public record MailProperties(
            @NotBlank String host,
            @Min(1) int port
    ) {
    }

    private final Validator validator;

    public ConfigurationPropertiesValidation(Validator validator) {
        this.validator = Objects.requireNonNull(validator, "validator");
    }

    /**
     * @ConfigurationProperties биндит обычные properties (mail.host, mail.port) в типобезопасный
     * record — без ручного environment.getProperty(...) на каждое поле.
     */
    public Set<ConstraintViolation<MailProperties>> bindAndValidate(Environment environment) {
        Objects.requireNonNull(environment, "environment");

        // ---8<--- solution
        MailProperties properties = Binder.get(environment)
                .bind("mail", MailProperties.class)
                .orElseThrow(() -> new IllegalStateException("mail.* properties are missing"));
        return validator.validate(properties);
        // --->8--- solution
    }
}
