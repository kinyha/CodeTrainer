package trainer.patterns.l3;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class FactoryMethodRegistryTest {

    @Test
    void createsANewInstanceOnEveryCall() {
        FactoryMethodRegistry registry = new FactoryMethodRegistry(Map.of("list", ArrayList::new));

        Object first = registry.create("list");
        Object second = registry.create("list");

        assertThat(first).isInstanceOf(List.class);
        assertThat(first).isNotSameAs(second);
    }

    @Test
    void rejectsUnknownType() {
        FactoryMethodRegistry registry = new FactoryMethodRegistry(Map.of());
        assertThatIllegalArgumentException().isThrownBy(() -> registry.create("missing"));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> new FactoryMethodRegistry(null));
        assertThatNullPointerException().isThrownBy(() -> new FactoryMethodRegistry(Map.of()).create(null));
    }
}
