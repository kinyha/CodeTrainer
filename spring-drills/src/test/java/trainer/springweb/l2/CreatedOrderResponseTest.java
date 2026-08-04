package trainer.springweb.l2;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class CreatedOrderResponseTest {

    @Test
    void returnsCreatedBodyAndAbsoluteLocation() {
        var order = new CreatedOrderResponse.OrderView(42, "NEW");

        var response = CreatedOrderResponse.created(order, URI.create("https://api.trainer.dev"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("https://api.trainer.dev/api/orders/42"));
        assertThat(response.getBody()).isEqualTo(order);
    }
}
