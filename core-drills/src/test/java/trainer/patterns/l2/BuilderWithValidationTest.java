package trainer.patterns.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BuilderWithValidationTest {

    @Test
    void buildsAddressFromChainedCalls() {
        BuilderWithValidation.Address address = new BuilderWithValidation.Builder()
                .street("Main St 1")
                .city("Berlin")
                .zip("10115")
                .build();

        assertThat(address).isEqualTo(new BuilderWithValidation.Address("Main St 1", "Berlin", "10115"));
    }

    @Test
    void rejectsMissingField() {
        assertThatNullPointerException().isThrownBy(() ->
                new BuilderWithValidation.Builder().city("Berlin").zip("10115").build());
    }

    @Test
    void rejectsBlankZip() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                new BuilderWithValidation.Builder().street("Main St 1").city("Berlin").zip(" ").build());
    }
}
