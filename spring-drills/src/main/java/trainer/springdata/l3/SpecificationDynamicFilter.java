package trainer.springdata.l3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// @task springdata.l3.SpecificationDynamicFilter
// @tags spring-data,specification,dynamic-query,jpql
// @time 25m
// @src  new
public final class SpecificationDynamicFilter {

    private SpecificationDynamicFilter() {
    }

    /**
     * Динамический WHERE: условие попадает в запрос, только если фильтр задан. Реальный
     * Specification&lt;T&gt; в Spring Data строит то же самое через CriteriaBuilder — здесь тот
     * же принцип явным JPQL с именованными параметрами, без склейки значений в текст запроса.
     */
    public static String jpql(Optional<String> status, Optional<Long> customerId) {
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(customerId, "customerId");

        // ---8<--- solution
        List<String> conditions = new ArrayList<>();
        if (status.isPresent()) {
            conditions.add("o.status = :status");
        }
        if (customerId.isPresent()) {
            conditions.add("o.customerId = :customerId");
        }
        String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
        return "SELECT o FROM OrderEntity o" + where + " ORDER BY o.id";
        // --->8--- solution
    }
}
