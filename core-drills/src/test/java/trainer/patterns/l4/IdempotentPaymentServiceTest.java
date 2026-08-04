package trainer.patterns.l4;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class IdempotentPaymentServiceTest {

    @Test
    void chargesOnceForConcurrentDuplicates() throws Exception {
        AtomicInteger charges = new AtomicInteger();
        var service = new IdempotentPaymentService(payment -> {
            charges.incrementAndGet();
            return new IdempotentPaymentService.Receipt("gateway-1");
        });
        var payment = new IdempotentPaymentService.Payment("pay-1", 7, new BigDecimal("42.00"));

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = new ArrayList<java.util.concurrent.Future<IdempotentPaymentService.Receipt>>();
            for (int index = 0; index < 100; index++) futures.add(executor.submit(() -> service.process(payment)));
            for (var future : futures) assertThat(future.get().gatewayId()).isEqualTo("gateway-1");
        }

        assertThat(charges).hasValue(1);
    }

    @Test
    void rejectsSameIdWithDifferentPayload() {
        var service = new IdempotentPaymentService(payment -> new IdempotentPaymentService.Receipt("gateway-1"));
        service.process(new IdempotentPaymentService.Payment("pay-1", 7, BigDecimal.TEN));

        assertThatIllegalArgumentException().isThrownBy(() ->
                service.process(new IdempotentPaymentService.Payment("pay-1", 8, BigDecimal.TEN)));
    }

    @Test
    void doesNotCacheGatewayFailure() {
        AtomicInteger attempts = new AtomicInteger();
        var service = new IdempotentPaymentService(payment -> {
            if (attempts.incrementAndGet() == 1) throw new IllegalStateException("temporary");
            return new IdempotentPaymentService.Receipt("gateway-2");
        });
        var payment = new IdempotentPaymentService.Payment("pay-2", 7, BigDecimal.ONE);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.process(payment))
                .isInstanceOf(IllegalStateException.class);
        assertThat(service.process(payment).gatewayId()).isEqualTo("gateway-2");
        assertThat(attempts).hasValue(2);
    }
}
