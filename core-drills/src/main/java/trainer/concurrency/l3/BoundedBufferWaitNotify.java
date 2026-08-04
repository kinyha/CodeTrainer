package trainer.concurrency.l3;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

// @task concurrency.l3.BoundedBufferWaitNotify
// @tags concurrency,wait,notifyAll,bounded-buffer
// @time 35m
// @src  new
// @doc  BoundedBufferWaitNotify.md
public final class BoundedBufferWaitNotify<T> {

    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;

    public BoundedBufferWaitNotify(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    public synchronized void put(T value) throws InterruptedException {
        Objects.requireNonNull(value, "value");

        // ---8<--- solution
        while (queue.size() == capacity) {
            wait(); // WHY: while повторно проверяет условие после spurious wakeup
        }
        queue.add(value);
        notifyAll();
        // --->8--- solution
    }

    public synchronized T take() throws InterruptedException {
        // ---8<--- solution
        while (queue.isEmpty()) {
            wait();
        }
        T value = queue.remove();
        notifyAll();
        return value;
        // --->8--- solution
    }

    public synchronized int size() {
        return queue.size();
    }
}
