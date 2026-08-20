package trainer.algorithms.l3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

// @task algorithms.l3.MergeIntervals
// @tags arrays,sorting,intervals
// @time 28m
// @src  new
public final class MergeIntervals {

    private MergeIntervals() {
    }

    /** Сливает пересекающиеся и соприкасающиеся интервалы. Порядок ввода не важен. */
    public static List<Interval> merge(List<Interval> intervals) {
        Objects.requireNonNull(intervals, "intervals");

        // ---8<--- solution
        List<Interval> sorted = new ArrayList<>(intervals);
        sorted.sort(Comparator.comparingInt(Interval::start));

        List<Interval> merged = new ArrayList<>();
        for (Interval interval : sorted) {
            if (merged.isEmpty() || merged.get(merged.size() - 1).end() < interval.start()) {
                merged.add(interval);
            } else {
                Interval last = merged.remove(merged.size() - 1);
                merged.add(new Interval(last.start(), Math.max(last.end(), interval.end())));
            }
        }
        return List.copyOf(merged);
        // --->8--- solution
    }

    public record Interval(int start, int end) {
        public Interval {
            if (end < start) {
                throw new IllegalArgumentException("end must not be before start");
            }
        }
    }
}
