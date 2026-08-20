package trainer.springweb.l2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

class BeanValidationAndDtoTest {

    private BeanValidationAndDto service;

    @BeforeEach
    void setUp() {
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        service = new BeanValidationAndDto(validator);
    }

    @Test
    void validRequestHasNoViolations() {
        assertThat(service.violationMessages(new BeanValidationAndDto.CreateProduct("Widget", 5))).isEmpty();
    }

    @Test
    void blankNameAndNegativeStockAreReported() {
        assertThat(service.violationMessages(new BeanValidationAndDto.CreateProduct(" ", -1))).hasSize(2);
    }
}
