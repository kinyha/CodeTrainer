package trainer.collections.l2;

import java.util.Objects;

// @task collections.l2.EqualsHashCodeContract
// @tags equals,hashCode,contract,HashSet
// @time 12m
// @src  new
public final class EqualsHashCodeContract {

    private EqualsHashCodeContract() {
    }

    public static final class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /** Равные объекты обязаны иметь равный hashCode — иначе HashSet/HashMap их не найдут. */
        @Override
        public boolean equals(Object other) {
            // ---8<--- solution
            if (this == other) {
                return true;
            }
            if (!(other instanceof Point that)) {
                return false;
            }
            return x == that.x && y == that.y;
            // --->8--- solution
        }

        @Override
        public int hashCode() {
            // ---8<--- solution
            return Objects.hash(x, y);
            // --->8--- solution
        }
    }
}
