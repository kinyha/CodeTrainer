package trainer.streams.l4;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CustomCollectorStudentStatsTest {

    @Test
    void computesCountSumMinMaxOnASmallList() {
        var stats = CustomCollectorStudentStats.gradeStats(List.of(70, 90, 80, 60));

        assertThat(stats.count()).isEqualTo(4);
        assertThat(stats.sum()).isEqualTo(300);
        assertThat(stats.min()).isEqualTo(60);
        assertThat(stats.max()).isEqualTo(90);
        assertThat(stats.average()).isEqualTo(75.0);
    }

    @Test
    void combinerMergesPartialResultsCorrectlyUnderParallelExecution() {
        List<Integer> grades = IntStream.rangeClosed(1, 5_000).boxed().toList();

        var stats = CustomCollectorStudentStats.gradeStats(grades);

        assertThat(stats.count()).isEqualTo(5_000);
        assertThat(stats.sum()).isEqualTo(5_000L * 5_001 / 2);
        assertThat(stats.min()).isEqualTo(1);
        assertThat(stats.max()).isEqualTo(5_000);
    }

    @Test
    void emptyListGivesZeroedStats() {
        var stats = CustomCollectorStudentStats.gradeStats(List.of());

        assertThat(stats.count()).isZero();
        assertThat(stats.sum()).isZero();
        assertThat(stats.average()).isZero();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CustomCollectorStudentStats.gradeStats(null));
    }
}
