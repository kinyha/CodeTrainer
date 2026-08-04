package trainer.springweb.l2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

// @task springweb.l2.CreatedOrderResponse
// @tags spring-mvc,ResponseEntity,location,created
// @time 12m
// @src  new
public final class CreatedOrderResponse {

    private CreatedOrderResponse() {
    }

    /** Создаёт 201 response с абсолютным Location /api/orders/{id}. */
    public static ResponseEntity<OrderView> created(OrderView order, URI requestBase) {
        Objects.requireNonNull(order, "order");
        Objects.requireNonNull(requestBase, "requestBase");

        // ---8<--- solution
        URI location = UriComponentsBuilder.fromUri(requestBase)
                .path("/api/orders/{id}")
                .buildAndExpand(order.id())
                .toUri();
        return ResponseEntity.created(location).body(order);
        // --->8--- solution
    }

    public record OrderView(long id, String status) {
    }
}
