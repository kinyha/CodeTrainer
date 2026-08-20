package trainer.patterns.l4;

// @task patterns.l4.SingletonThreadSafeVariants
// @tags patterns,singleton,initialization-on-demand-holder,thread-safe
// @time 35m
// @src  new
public final class SingletonThreadSafeVariants {

    private final long createdAtNanos = System.nanoTime();

    private SingletonThreadSafeVariants() {
    }

    /**
     * Initialization-on-demand holder: вложенный класс Holder грузится JVM только при
     * первом обращении к INSTANCE — classloader сам гарантирует потокобезопасность
     * и однократность инициализации, без synchronized и volatile.
     */
    public static SingletonThreadSafeVariants getInstance() {
        // ---8<--- solution
        return Holder.INSTANCE;
        // --->8--- solution
    }

    long createdAtNanos() {
        return createdAtNanos;
    }

    private static final class Holder {
        private static final SingletonThreadSafeVariants INSTANCE = new SingletonThreadSafeVariants();
    }
}
