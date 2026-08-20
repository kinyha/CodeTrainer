package trainer.concurrency.l3;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// @task concurrency.l3.ReentrantLockTryLock
// @tags concurrency,ReentrantLock,tryLock,timeout
// @time 25m
// @src  new
public final class ReentrantLockTryLock {

    final Lock lock = new ReentrantLock();
    private int value;

    /** Не блокируется навсегда: если lock не достался за timeout, сдаёмся вместо дедлока. */
    public boolean tryIncrement(long timeout, TimeUnit unit) throws InterruptedException {
        Objects.requireNonNull(unit, "unit");

        // ---8<--- solution
        if (!lock.tryLock(timeout, unit)) {
            return false;
        }
        try {
            value++;
            return true;
        } finally {
            lock.unlock();
        }
        // --->8--- solution
    }

    public int get() {
        lock.lock();
        try {
            return value;
        } finally {
            lock.unlock();
        }
    }
}
