package trainer.springcore.l2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationPropertiesValidationTest {

    private ConfigurationPropertiesValidation service;

    @BeforeEach
    void setUp() {
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        service = new ConfigurationPropertiesValidation(validator);
    }

    @Test
    void bindsValidPropertiesWithoutViolations() {
        var environment = environmentWith(Map.of("mail.host", "smtp.example.com", "mail.port", "587"));
        assertThat(service.bindAndValidate(environment)).isEmpty();
    }

    @Test
    void reportsViolationForBlankHost() {
        var environment = environmentWith(Map.of("mail.host", " ", "mail.port", "587"));
        assertThat(service.bindAndValidate(environment)).hasSize(1);
    }

    private static StandardEnvironment environmentWith(Map<String, Object> properties) {
        var environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("test", properties));
        return environment;
    }
}
