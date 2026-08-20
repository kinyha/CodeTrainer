package trainer.algorithms.l4;

import java.util.HashMap;
import java.util.Map;

// @task algorithms.l4.LruCacheHandRolled
// @tags hash-map,doubly-linked-list,cache,lru
// @time 60m
// @src  new
public final class LruCacheHandRolled {

    private final int capacity;
    private final Map<Integer, Node> nodesByKey = new HashMap<>();
    private final Node head = new Node(0, 0); // sentinel: сразу за ним — самый свежий узел
    private final Node tail = new Node(0, 0); // sentinel: сразу перед ним — самый старый узел

    public LruCacheHandRolled(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    /** -1, если ключа нет. Успешное чтение делает запись самой недавно использованной. */
    public int get(int key) {
        // ---8<--- solution
        Node node = nodesByKey.get(key);
        if (node == null) {
            return -1;
        }
        moveToFront(node);
        return node.value;
        // --->8--- solution
    }

    /** Вставка или обновление тоже освежает запись. При переполнении вытесняет самую старую. */
    public void put(int key, int value) {
        // ---8<--- solution
        Node existing = nodesByKey.get(key);
        if (existing != null) {
            existing.value = value;
            moveToFront(existing);
            return;
        }
        if (nodesByKey.size() == capacity) {
            Node lru = tail.prev;
            unlink(lru);
            nodesByKey.remove(lru.key);
        }
        Node node = new Node(key, value);
        nodesByKey.put(key, node);
        insertAfterHead(node);
        // --->8--- solution
    }

    private void moveToFront(Node node) {
        unlink(node);
        insertAfterHead(node);
    }

    private void unlink(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertAfterHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private static final class Node {
        final int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
