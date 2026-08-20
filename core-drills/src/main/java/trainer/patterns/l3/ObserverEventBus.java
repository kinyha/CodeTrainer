package trainer.patterns.l3;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

// @task patterns.l3.ObserverEventBus
// @tags patterns,observer,event-bus,pub-sub
// @time 25m
// @src  new
public final class ObserverEventBus<T> {

    private final List<Consumer<T>> subscribers = new CopyOnWriteArrayList<>();

    public void subscribe(Consumer<T> subscriber) {
        subscribers.add(Objects.requireNonNull(subscriber, "subscriber"));
    }

    /** publish не знает и не должен знать, СКОЛЬКО подписчиков и что они делают. */
    public void publish(T event) {
        Objects.requireNonNull(event, "event");

        // ---8<--- solution
        for (Consumer<T> subscriber : subscribers) {
            subscriber.accept(event);
        }
        // --->8--- solution
    }
}
