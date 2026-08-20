package trainer.collections.l3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class NestedMapAggregationTest {

    @Test
    void sumsAmountsByRegionAndCategory() {
        List<NestedMapAggregation.Sale> sales = List.of(
                new NestedMapAggregation.Sale("EU", "books", 10),
                new NestedMapAggregation.Sale("EU", "books", 5),
                new NestedMapAggregation.Sale("EU", "toys", 7),
                new NestedMapAggregation.Sale("US", "books", 20));

        var result = NestedMapAggregation.revenueByRegionAndCategory(sales);

        assertThat(result.get("EU")).containsEntry("books", 15L).containsEntry("toys", 7L);
        assertThat(result.get("US")).containsEntry("books", 20L);
    }

    @Test
    void emptyListGivesEmptyMap() {
        assertThat(NestedMapAggregation.revenueByRegionAndCategory(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> NestedMapAggregation.revenueByRegionAndCategory(null));
    }
}
