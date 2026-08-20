package trainer.concurrency.l2;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class VolatileStopFlagTest {

    @Test
    void stopsTheLoopWhenAnotherThreadCallsStop() throws InterruptedException {
        VolatileStopFlag flag = new VolatileStopFlag();
        AtomicInteger ticks = new AtomicInteger();
        CountDownLatch tenTicksSeen = new CountDownLatch(10);

        Thread runner = new Thread(() -> flag.run(() -> {
            ticks.incrementAndGet();
            tenTicksSeen.countDown();
        }));
        runner.start();

        assertThat(tenTicksSeen.await(2, TimeUnit.SECONDS)).isTrue();
        flag.stop();
        runner.join(2000);

        assertThat(runner.isAlive()).isFalse();
        assertThat(ticks.get()).isGreaterThanOrEqualTo(10);
    }

    @Test
    void rejectsNullTick() {
        assertThatNullPointerException().isThrownBy(() -> new VolatileStopFlag().run(null));
    }
}
