package trainer.springcore.l2;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class ProfilesConditionalTest {

    @Test
    void usesRealGatewayWhenProdProfileIsActive() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("prod");
            context.register(ProfilesConditional.Config.class);
            context.refresh();

            var gateway = context.getBean(ProfilesConditional.PaymentGateway.class);
            assertThat(gateway.charge(500)).isEqualTo("charged:500");
        }
    }

    @Test
    void usesFakeGatewayWhenProdProfileIsNotActive() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("dev");
            context.register(ProfilesConditional.Config.class);
            context.refresh();

            var gateway = context.getBean(ProfilesConditional.PaymentGateway.class);
            assertThat(gateway.charge(500)).isEqualTo("fake-charged:500");
        }
    }
}
