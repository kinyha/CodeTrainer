package trainer.springweb.l3;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

// @task springweb.l3.PaginationAndSorting
// @tags spring-mvc,pageable,sorting,pagination
// @time 25m
// @src  new
public final class PaginationAndSorting {

    private PaginationAndSorting() {
    }

    /**
     * Pageable уже несёт номер страницы, размер и сортировку — их не нужно принимать
     * отдельными параметрами. Здесь эмулируем то, что в реальном коде делает Repository.
     */
    public static Page<String> page(List<String> names, Pageable pageable) {
        Objects.requireNonNull(names, "names");
        Objects.requireNonNull(pageable, "pageable");

        // ---8<--- solution
        List<String> sorted = pageable.getSort().isSorted()
                ? names.stream().sorted().toList()
                : names;
        int start = Math.min((int) pageable.getOffset(), sorted.size());
        int end = Math.min(start + pageable.getPageSize(), sorted.size());
        return new PageImpl<>(sorted.subList(start, end), pageable, sorted.size());
        // --->8--- solution
    }
}
