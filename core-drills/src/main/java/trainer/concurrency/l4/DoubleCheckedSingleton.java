package trainer.concurrency.l4;

// @task concurrency.l4.DoubleCheckedSingleton
// @tags concurrency,singleton,double-checked-locking,volatile
// @time 45m
// @src  new
public final class DoubleCheckedSingleton {

    private static volatile DoubleCheckedSingleton instance;

    private final long createdAtNanos = System.nanoTime();

    private DoubleCheckedSingleton() {
    }

    /**
     * Первая проверка — без блокировки, для быстрого пути. volatile на поле обязателен:
     * без него другой поток может увидеть instance != null, но объект ещё не полностью
     * сконструирован — запись в поле и запись полей конструктора могут переупорядочиться.
     */
    public static DoubleCheckedSingleton getInstance() {
        // ---8<--- solution
        DoubleCheckedSingleton result = instance;
        if (result == null) {
            synchronized (DoubleCheckedSingleton.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DoubleCheckedSingleton();
                }
            }
        }
        return result;
        // --->8--- solution
    }

    long createdAtNanos() {
        return createdAtNanos;
    }
}
