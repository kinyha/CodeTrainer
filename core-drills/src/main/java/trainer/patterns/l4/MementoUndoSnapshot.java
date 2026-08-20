package trainer.patterns.l4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

// @task patterns.l4.MementoUndoSnapshot
// @tags patterns,memento,snapshot,defensive-copy
// @time 35m
// @src  new
public final class MementoUndoSnapshot {

    public record Memento(List<String> items) {
        public Memento {
            items = List.copyOf(items); // WHY: снимок обязан быть иммутабельным, иначе он "уедет" вместе с оригиналом
        }
    }

    private final List<String> items = new ArrayList<>();
    private final Deque<Memento> history = new ArrayDeque<>();

    public void add(String item) {
        Objects.requireNonNull(item, "item");
        history.push(new Memento(items));
        items.add(item);
    }

    /** Откатывает к состоянию ДО последнего add(). Снимок неизменяем — новый add() его не испортит. */
    public void undo() {
        // ---8<--- solution
        if (history.isEmpty()) {
            throw new IllegalStateException("nothing to undo");
        }
        Memento previous = history.pop();
        items.clear();
        items.addAll(previous.items());
        // --->8--- solution
    }

    public List<String> items() {
        return List.copyOf(items);
    }
}
