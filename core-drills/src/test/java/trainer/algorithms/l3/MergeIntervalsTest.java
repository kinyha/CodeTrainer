package trainer.algorithms.l3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MergeIntervalsTest {

    @Test
    void mergesOverlappingIntervalsRegardlessOfInputOrder() {
        List<MergeIntervals.Interval> input = List.of(
                new MergeIntervals.Interval(8, 10),
                new MergeIntervals.Interval(1, 3),
                new MergeIntervals.Interval(2, 6),
                new MergeIntervals.Interval(15, 18));

        assertThat(MergeIntervals.merge(input)).containsExactly(
                new MergeIntervals.Interval(1, 6),
                new MergeIntervals.Interval(8, 10),
                new MergeIntervals.Interval(15, 18));
    }

    @Test
    void touchingIntervalsAreMerged() {
        List<MergeIntervals.Interval> input = List.of(
                new MergeIntervals.Interval(1, 4), new MergeIntervals.Interval(4, 5));

        assertThat(MergeIntervals.merge(input)).containsExactly(new MergeIntervals.Interval(1, 5));
    }

    @Test
    void emptyListGivesEmptyList() {
        assertThat(MergeIntervals.merge(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> MergeIntervals.merge(null));
    }
}
