package trainer.algorithms.l4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

// @task algorithms.l4.TopologicalOrderKahn
// @tags graph,bfs,kahn,in-degree,cycle-detection
// @time 50m
// @src  new
public final class TopologicalOrderKahn {

    private TopologicalOrderKahn() {
    }

    /**
     * Алгоритм Кана: сначала в очередь попадают узлы без входящих рёбер, у их соседей
     * снимается по одному входящему ребру, повторяем. Optional.empty(), если в графе
     * есть цикл — тогда не все узлы наберут in-degree 0.
     */
    public static Optional<List<String>> order(Map<String, List<String>> graph) {
        Objects.requireNonNull(graph, "graph");

        // ---8<--- solution
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : graph.keySet()) {
            inDegree.putIfAbsent(node, 0);
            for (String dependent : graph.get(node)) {
                inDegree.merge(dependent, 1, Integer::sum);
            }
        }

        Deque<String> ready = new ArrayDeque<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                ready.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!ready.isEmpty()) {
            String node = ready.poll();
            result.add(node);
            for (String dependent : graph.getOrDefault(node, List.of())) {
                if (inDegree.merge(dependent, -1, Integer::sum) == 0) {
                    ready.add(dependent);
                }
            }
        }

        return result.size() == inDegree.size() ? Optional.of(result) : Optional.empty();
        // --->8--- solution
    }
}
