package trainer.springdata.l5;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class PlaceOrderWithOutboxTest {

    private static final Instant NOW = Instant.parse("2026-08-04T12:00:00Z");
    private static final UUID EVENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000007");

    @Test
    void savesOrderThenOutboxEventWithDeterministicPayload() {
        PlaceOrderWithOutbox.OrderRepository orders = mock();
        PlaceOrderWithOutbox.OutboxRepository outbox = mock();
        var service = new PlaceOrderWithOutbox(
                orders, outbox, Clock.fixed(NOW, ZoneOffset.UTC), () -> EVENT_ID);

        var result = service.place(10, 20, new BigDecimal("42.50"));

        var order = new PlaceOrderWithOutbox.Order(10, 20, new BigDecimal("42.50"), "PLACED");
        var event = new PlaceOrderWithOutbox.OutboxEvent(
                EVENT_ID, "Order", "10", "OrderPlaced",
                "{\"orderId\":10,\"customerId\":20,\"total\":\"42.50\"}", NOW);
        InOrder writes = inOrder(orders, outbox);
        writes.verify(orders).save(order);
        writes.verify(outbox).save(event);
        assertThat(result).isEqualTo(order);
    }

    @Test
    void transactionCoversBothWrites() throws Exception {
        var method = PlaceOrderWithOutbox.class.getMethod("place", long.class, long.class, BigDecimal.class);
        assertThat(method.getAnnotation(Transactional.class)).isNotNull();
    }
}
