package trainer.springcore.l3;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Objects;

// @task springcore.l3.QualifierAndPrimary
// @tags spring-core,qualifier,primary,ambiguous-bean
// @time 20m
// @src  new
public final class QualifierAndPrimary {

    public interface Notifier {
        String send(String message);
    }

    @Configuration
    public static class Config {

        @Bean
        @Primary
        public Notifier emailNotifier() {
            return message -> "email:" + message;
        }

        @Bean
        @Qualifier("sms")
        public Notifier smsNotifier() {
            return message -> "sms:" + message;
        }
    }

    private final Notifier defaultNotifier;
    private final Notifier smsNotifier;

    /**
     * Два бина одного типа — без @Primary/@Qualifier Spring не смог бы выбрать, какой внедрять,
     * и упал бы с NoUniqueBeanDefinitionException при старте контекста.
     */
    public QualifierAndPrimary(Notifier defaultNotifier, @Qualifier("sms") Notifier smsNotifier) {
        // ---8<--- solution
        this.defaultNotifier = Objects.requireNonNull(defaultNotifier, "defaultNotifier");
        this.smsNotifier = Objects.requireNonNull(smsNotifier, "smsNotifier");
        // --->8--- solution
    }

    public String sendDefault(String message) {
        return defaultNotifier.send(message);
    }

    public String sendSms(String message) {
        return smsNotifier.send(message);
    }
}
