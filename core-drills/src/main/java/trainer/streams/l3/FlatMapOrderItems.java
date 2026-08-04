package trainer.streams.l3;

import trainer.fixtures.sales.Order;

import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

// @task streams.l3.FlatMapOrderItems
// @tags streams,flatMap,groupingBy,summingInt
// @time 25m
// @src  new
// @doc  FlatMapOrderItems.md
public final class FlatMapOrderItems {

    private FlatMapOrderItems() {
    }

    /** Суммирует количество каждого товара во всех заказах, сортируя по productId. */
    public static List<ProductQuantity> summarize(List<Order> orders) {
        Objects.requireNonNull(orders, "orders");

        // ---8<--- solution
        return orders.stream()
                .peek(order -> Objects.requireNonNull(order, "order"))
                .flatMap(order -> order.items().stream())
                .collect(Collectors.groupingBy(
                        item -> new Product(item.productId(), item.productName()),
                        TreeMap::new,
                        Collectors.summingInt(item -> item.quantity())
                ))
                .entrySet().stream()
                .map(entry -> new ProductQuantity(entry.getKey().id(), entry.getKey().name(), entry.getValue()))
                .toList();
        // --->8--- solution
    }

    private record Product(long id, String name) implements Comparable<Product> {
        @Override
        public int compareTo(Product other) {
            return Long.compare(id, other.id);
        }
    }

    public record ProductQuantity(long productId, String productName, int quantity) {
    }
}
