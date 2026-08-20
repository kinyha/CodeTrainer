package trainer.springcore.l3;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class QualifierAndPrimaryTest {

    @Test
    void injectsPrimaryBeanForTheUnqualifiedParameterAndQualifiedBeanForTheOther() {
        try (var context = new AnnotationConfigApplicationContext(
                QualifierAndPrimary.Config.class, QualifierAndPrimary.class)) {
            var service = context.getBean(QualifierAndPrimary.class);

            assertThat(service.sendDefault("hi")).isEqualTo("email:hi");
            assertThat(service.sendSms("hi")).isEqualTo("sms:hi");
        }
    }
}
