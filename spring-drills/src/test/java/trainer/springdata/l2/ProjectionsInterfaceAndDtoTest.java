package trainer.springdata.l2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ProjectionsInterfaceAndDtoTest {

    @Test
    void keepsOnlyIdAndName() {
        var customer = new ProjectionsInterfaceAndDto.Customer(1, "Ada", "ada@example.com", "VIP, handle with care");
        assertThat(ProjectionsInterfaceAndDto.toSummaries(List.of(customer)))
                .containsExactly(new ProjectionsInterfaceAndDto.CustomerSummary(1, "Ada"));
    }

    @Test
    void emptyListGivesEmptyList() {
        assertThat(ProjectionsInterfaceAndDto.toSummaries(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ProjectionsInterfaceAndDto.toSummaries(null));
    }
}
