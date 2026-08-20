package trainer.springweb.l3;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PaginationAndSortingTest {

    private final List<String> names = List.of("Cleo", "Ada", "Bob", "Dan", "Eve");

    @Test
    void returnsRequestedPageUnsortedByDefault() {
        var result = PaginationAndSorting.page(names, PageRequest.of(0, 2));

        assertThat(result.getContent()).containsExactly("Cleo", "Ada");
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(3);
    }

    @Test
    void sortsWhenPageableCarriesASort() {
        var result = PaginationAndSorting.page(names, PageRequest.of(0, 3, Sort.by("name")));
        assertThat(result.getContent()).containsExactly("Ada", "Bob", "Cleo");
    }

    @Test
    void lastPageIsPartial() {
        var result = PaginationAndSorting.page(names, PageRequest.of(2, 2));
        assertThat(result.getContent()).containsExactly("Eve");
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> PaginationAndSorting.page(null, PageRequest.of(0, 1)));
        assertThatNullPointerException().isThrownBy(() -> PaginationAndSorting.page(names, null));
    }
}
