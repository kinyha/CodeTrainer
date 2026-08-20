package trainer.concurrency.l4;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// @task concurrency.l4.BoundedBufferCondition
// @tags concurrency,Lock,Condition,bounded-buffer
// @time 45m
// @src  new
public final class BoundedBufferCondition<T> {

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;

    public BoundedBufferCondition(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    /** Два отдельных Condition — producer'ы и consumer'ы будятся только по своему сигналу, не все разом. */
    public void put(T value) throws InterruptedException {
        Objects.requireNonNull(value, "value");

        // ---8<--- solution
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(value);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
        // --->8--- solution
    }

    public T take() throws InterruptedException {
        // ---8<--- solution
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T value = queue.remove();
            notFull.signal();
            return value;
        } finally {
            lock.unlock();
        }
        // --->8--- solution
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}
