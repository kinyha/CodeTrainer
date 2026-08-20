package trainer.patterns.l3;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

// @task patterns.l3.CommandPatternUndoRedo
// @tags patterns,command,undo,encapsulated-action
// @time 28m
// @src  new
public final class CommandPatternUndoRedo {

    public interface Command {
        void execute();

        void undo();
    }

    private final Deque<Command> history = new ArrayDeque<>();

    public void run(Command command) {
        Objects.requireNonNull(command, "command");
        command.execute();
        history.push(command);
    }

    /** Undo не знает, ЧТО делает конкретная команда — она сама умеет откатить себя. */
    public void undoLast() {
        // ---8<--- solution
        if (history.isEmpty()) {
            throw new IllegalStateException("nothing to undo");
        }
        history.pop().undo();
        // --->8--- solution
    }
}
