package trainer.springcore.l2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// @task springcore.l2.ProfilesConditional
// @tags spring-core,profile,conditional,environment
// @time 15m
// @src  new
public final class ProfilesConditional {

    private ProfilesConditional() {
    }

    public interface PaymentGateway {
        String charge(long amountCents);
    }

    @Configuration
    public static class Config {

        /** Активен только в проде — реальный внешний вызов недопустим в тестах/dev. */
        @Bean
        @Profile("prod")
        public PaymentGateway realGateway() {
            // ---8<--- solution
            return amountCents -> "charged:" + amountCents;
            // --->8--- solution
        }

        /** Активен, когда "prod" НЕ выбран — !prod читается как отрицание профиля. */
        @Bean
        @Profile("!prod")
        public PaymentGateway fakeGateway() {
            // ---8<--- solution
            return amountCents -> "fake-charged:" + amountCents;
            // --->8--- solution
        }
    }
}
