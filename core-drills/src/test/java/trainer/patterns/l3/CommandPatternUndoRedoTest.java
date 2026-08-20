package trainer.patterns.l3;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class CommandPatternUndoRedoTest {

    @Test
    void undoesTheLastCommandFirst() {
        CommandPatternUndoRedo editor = new CommandPatternUndoRedo();
        List<String> log = new ArrayList<>();

        editor.run(command(log, "add-a"));
        editor.run(command(log, "add-b"));

        editor.undoLast();
        editor.undoLast();

        assertThat(log).containsExactly("execute add-a", "execute add-b", "undo add-b", "undo add-a");
    }

    @Test
    void throwsWhenNothingToUndo() {
        CommandPatternUndoRedo editor = new CommandPatternUndoRedo();
        assertThatIllegalStateException().isThrownBy(editor::undoLast);
    }

    private static CommandPatternUndoRedo.Command command(List<String> log, String name) {
        return new CommandPatternUndoRedo.Command() {
            @Override
            public void execute() {
                log.add("execute " + name);
            }

            @Override
            public void undo() {
                log.add("undo " + name);
            }
        };
    }
}
