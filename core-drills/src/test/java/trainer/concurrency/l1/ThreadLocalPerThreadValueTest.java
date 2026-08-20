package trainer.concurrency.l1;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadLocalPerThreadValueTest {

    @Test
    void incrementsSequentiallyWithinTheCurrentThread() {
        int first = ThreadLocalPerThreadValue.incrementAndGet();
        int second = ThreadLocalPerThreadValue.incrementAndGet();
        assertThat(second).isEqualTo(first + 1);
    }

    @Test
    void otherThreadsStartFromTheirOwnZero() throws InterruptedException {
        ThreadLocalPerThreadValue.incrementAndGet(); // трогаем счётчик текущего потока

        AtomicInteger otherThreadResult = new AtomicInteger();
        Thread other = new Thread(() -> otherThreadResult.set(ThreadLocalPerThreadValue.incrementAndGet()));
        other.start();
        other.join();

        assertThat(otherThreadResult.get()).isEqualTo(1);
    }
}
