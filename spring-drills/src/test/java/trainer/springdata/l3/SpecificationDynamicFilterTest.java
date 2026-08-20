package trainer.springdata.l3;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class SpecificationDynamicFilterTest {

    @Test
    void addsBothConditionsWhenBothFiltersArePresent() {
        String jpql = SpecificationDynamicFilter.jpql(Optional.of("DELIVERED"), Optional.of(7L));
        assertThat(jpql).isEqualTo("SELECT o FROM OrderEntity o WHERE o.status = :status AND o.customerId = :customerId ORDER BY o.id");
    }

    @Test
    void addsOnlyStatusConditionWhenCustomerFilterIsAbsent() {
        String jpql = SpecificationDynamicFilter.jpql(Optional.of("DELIVERED"), Optional.empty());
        assertThat(jpql).isEqualTo("SELECT o FROM OrderEntity o WHERE o.status = :status ORDER BY o.id");
    }

    @Test
    void omitsWhereClauseWhenNoFiltersArePresent() {
        String jpql = SpecificationDynamicFilter.jpql(Optional.empty(), Optional.empty());
        assertThat(jpql).isEqualTo("SELECT o FROM OrderEntity o ORDER BY o.id");
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> SpecificationDynamicFilter.jpql(null, Optional.empty()));
        assertThatNullPointerException().isThrownBy(() -> SpecificationDynamicFilter.jpql(Optional.empty(), null));
    }
}
