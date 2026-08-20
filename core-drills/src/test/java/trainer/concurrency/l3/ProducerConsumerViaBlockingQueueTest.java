package trainer.concurrency.l3;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class ProducerConsumerViaBlockingQueueTest {

    @Test
    void collectsProducedValuesUntilPoisonPill() throws Exception {
        ProducerConsumerViaBlockingQueue channel = new ProducerConsumerViaBlockingQueue();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var consumer = executor.submit(channel::consumeUntilPoisonPill);

            channel.produce(1);
            channel.produce(2);
            channel.produce(3);
            channel.stopConsumers();

            List<Integer> collected = consumer.get();
            assertThat(collected).containsExactly(1, 2, 3);
        }
    }

    @Test
    void noValuesProducedGivesEmptyList() throws Exception {
        ProducerConsumerViaBlockingQueue channel = new ProducerConsumerViaBlockingQueue();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var consumer = executor.submit(channel::consumeUntilPoisonPill);
            channel.stopConsumers();
            assertThat(consumer.get()).isEmpty();
        }
    }
}
