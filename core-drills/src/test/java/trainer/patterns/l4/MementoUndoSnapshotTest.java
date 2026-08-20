package trainer.patterns.l4;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class MementoUndoSnapshotTest {

    @Test
    void undoRestoresThePreviousSnapshot() {
        MementoUndoSnapshot editor = new MementoUndoSnapshot();
        editor.add("a");
        editor.add("b");
        editor.add("c");

        editor.undo();
        assertThat(editor.items()).containsExactly("a", "b");

        editor.undo();
        assertThat(editor.items()).containsExactly("a");
    }

    @Test
    void snapshotsStayIndependentOfLaterMutation() {
        MementoUndoSnapshot editor = new MementoUndoSnapshot();
        editor.add("a");
        editor.add("b");
        editor.undo();
        editor.add("x");

        assertThat(editor.items()).containsExactly("a", "x");
    }

    @Test
    void throwsWhenNothingToUndo() {
        MementoUndoSnapshot editor = new MementoUndoSnapshot();
        assertThatIllegalStateException().isThrownBy(editor::undo);
    }
}
