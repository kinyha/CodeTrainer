package trainer.concurrency.l3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// @task concurrency.l3.ProducerConsumerViaBlockingQueue
// @tags concurrency,BlockingQueue,producer-consumer,poison-pill
// @time 28m
// @src  new
public final class ProducerConsumerViaBlockingQueue {

    private static final Integer POISON_PILL = Integer.MIN_VALUE;

    private final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(16);

    /** put() блокируется, если очередь полна — ручную синхронизацию, как в wait/notify, писать не нужно. */
    public void produce(int value) throws InterruptedException {
        queue.put(value);
    }

    public void stopConsumers() throws InterruptedException {
        queue.put(POISON_PILL);
    }

    /** Poison pill — стандартный способ сказать consumer'у «данных больше не будет». */
    public List<Integer> consumeUntilPoisonPill() throws InterruptedException {
        // ---8<--- solution
        List<Integer> collected = new ArrayList<>();
        while (true) {
            Integer value = queue.take();
            if (value.equals(POISON_PILL)) {
                return collected;
            }
            collected.add(value);
        }
        // --->8--- solution
    }
}
