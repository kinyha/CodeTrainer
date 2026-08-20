package trainer.patterns.l3;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

// @task patterns.l3.StateMachineOrderTransitions
// @tags patterns,state-machine,enum,valid-transitions
// @time 25m
// @src  new
public final class StateMachineOrderTransitions {

    public enum Status {
        NEW, PAID, SHIPPED, DELIVERED, CANCELLED
    }

    private static final Map<Status, Set<Status>> ALLOWED = new EnumMap<>(Status.class);

    static {
        ALLOWED.put(Status.NEW, EnumSet.of(Status.PAID, Status.CANCELLED));
        ALLOWED.put(Status.PAID, EnumSet.of(Status.SHIPPED, Status.CANCELLED));
        ALLOWED.put(Status.SHIPPED, EnumSet.of(Status.DELIVERED));
        ALLOWED.put(Status.DELIVERED, EnumSet.noneOf(Status.class));
        ALLOWED.put(Status.CANCELLED, EnumSet.noneOf(Status.class));
    }

    private StateMachineOrderTransitions() {
    }

    /** Таблица переходов — единственный источник правды; никаких if(from==X && to==Y) в коде. */
    public static Status transition(Status from, Status to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");

        // ---8<--- solution
        if (!ALLOWED.get(from).contains(to)) {
            throw new IllegalStateException("cannot move from " + from + " to " + to);
        }
        return to;
        // --->8--- solution
    }
}
