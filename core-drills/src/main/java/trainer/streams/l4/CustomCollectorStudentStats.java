package trainer.streams.l4;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;

// @task streams.l4.CustomCollectorStudentStats
// @tags streams,Collector,custom,combiner
// @time 50m
// @src  new
public final class CustomCollectorStudentStats {

    private CustomCollectorStudentStats() {
    }

    /**
     * Свой Collector.of(supplier, accumulator, combiner, finisher). combiner обязателен и
     * должен корректно сливать частичные результаты — без него parallelStream() молча
     * даёт неверный результат, а не ошибку компиляции.
     */
    public static Stats gradeStats(List<Integer> grades) {
        Objects.requireNonNull(grades, "grades");

        // ---8<--- solution
        Collector<Integer, ?, Stats> collector = Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                Accumulator::toStats
        );
        return grades.parallelStream().collect(collector);
        // --->8--- solution
    }

    public record Stats(long count, long sum, int min, int max) {
        public double average() {
            return count == 0 ? 0.0 : (double) sum / count;
        }
    }

    private static final class Accumulator {
        private long count = 0;
        private long sum = 0;
        private int min = Integer.MAX_VALUE;
        private int max = Integer.MIN_VALUE;

        void add(int grade) {
            count++;
            sum += grade;
            min = Math.min(min, grade);
            max = Math.max(max, grade);
        }

        Accumulator combine(Accumulator other) {
            count += other.count;
            sum += other.sum;
            min = Math.min(min, other.min);
            max = Math.max(max, other.max);
            return this;
        }

        Stats toStats() {
            return new Stats(count, sum, count == 0 ? 0 : min, count == 0 ? 0 : max);
        }
    }
}
