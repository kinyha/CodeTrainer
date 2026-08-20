package trainer.collections.l3;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @task collections.l3.NestedMapAggregation
// @tags Map,nested,computeIfAbsent,aggregation
// @time 25m
// @src  new
public final class NestedMapAggregation {

    private NestedMapAggregation() {
    }

    /** Map[регион][категория] = сумма выручки; порядок обхода — по первому появлению ключа. */
    public static Map<String, Map<String, Long>> revenueByRegionAndCategory(List<Sale> sales) {
        Objects.requireNonNull(sales, "sales");

        // ---8<--- solution
        Map<String, Map<String, Long>> result = new LinkedHashMap<>();
        for (Sale sale : sales) {
            Objects.requireNonNull(sale, "sale");
            result.computeIfAbsent(sale.region(), ignored -> new LinkedHashMap<>())
                    .merge(sale.category(), sale.amount(), Long::sum);
        }
        return result;
        // --->8--- solution
    }

    public record Sale(String region, String category, long amount) {
    }
}
