package trainer.fixtures.sales;

import java.math.BigDecimal;
import java.util.Objects;

public record OrderItem(long productId, String productName, int quantity, BigDecimal price) {
    public OrderItem {
        Objects.requireNonNull(productName, "productName");
        Objects.requireNonNull(price, "price");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
    }

    public BigDecimal total() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
