package trainer.concurrency.l2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class AtomicCounterTest {

    @Test
    void doesNotLoseConcurrentIncrements() throws Exception {
        AtomicCounter counter = new AtomicCounter();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var tasks = new ArrayList<java.util.concurrent.Future<Long>>();
            for (int index = 0; index < 1_000; index++) {
                tasks.add(executor.submit(counter::incrementAndGet));
            }
            for (var task : tasks) {
                task.get();
            }
        }

        assertThat(counter.get()).isEqualTo(1_000);
    }
}
