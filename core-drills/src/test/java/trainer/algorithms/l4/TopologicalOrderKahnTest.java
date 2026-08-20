package trainer.algorithms.l4;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class TopologicalOrderKahnTest {

    @Test
    void ordersNodesSoEveryEdgeGoesForward() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("compile", List.of("test"));
        graph.put("test", List.of("package"));
        graph.put("lint", List.of("package"));
        graph.put("package", List.of());

        Optional<List<String>> order = TopologicalOrderKahn.order(graph);

        assertThat(order).isPresent();
        List<String> result = order.get();
        assertThat(result).containsExactlyInAnyOrder("compile", "test", "lint", "package");
        assertThat(result.indexOf("compile")).isLessThan(result.indexOf("test"));
        assertThat(result.indexOf("test")).isLessThan(result.indexOf("package"));
        assertThat(result.indexOf("lint")).isLessThan(result.indexOf("package"));
    }

    @Test
    void detectsCycleAndReturnsEmpty() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("a", List.of("b"));
        graph.put("b", List.of("c"));
        graph.put("c", List.of("a"));

        assertThat(TopologicalOrderKahn.order(graph)).isEmpty();
    }

    @Test
    void handlesNodeWithNoEdges() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("solo", List.of());

        assertThat(TopologicalOrderKahn.order(graph)).contains(List.of("solo"));
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> TopologicalOrderKahn.order(null));
    }
}
