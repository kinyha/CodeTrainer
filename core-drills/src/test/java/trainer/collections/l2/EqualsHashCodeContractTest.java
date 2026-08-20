package trainer.collections.l2;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EqualsHashCodeContractTest {

    @Test
    void equalPointsAreEqualAndShareHashCode() {
        var a = new EqualsHashCodeContract.Point(1, 2);
        var b = new EqualsHashCodeContract.Point(1, 2);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void differentCoordinatesAreNotEqual() {
        var a = new EqualsHashCodeContract.Point(1, 2);
        var b = new EqualsHashCodeContract.Point(2, 1);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void isNotEqualToNullOrOtherType() {
        var a = new EqualsHashCodeContract.Point(1, 2);

        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("1,2");
    }

    @Test
    void worksAsHashSetKey() {
        Set<EqualsHashCodeContract.Point> points = new HashSet<>();
        points.add(new EqualsHashCodeContract.Point(1, 2));
        points.add(new EqualsHashCodeContract.Point(1, 2));

        assertThat(points).hasSize(1);
    }
}
