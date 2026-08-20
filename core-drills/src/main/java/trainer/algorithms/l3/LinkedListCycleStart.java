package trainer.algorithms.l3;

// @task algorithms.l3.LinkedListCycleStart
// @tags linked-list,two-pointers,floyd
// @time 35m
// @src  new
public final class LinkedListCycleStart {

    private LinkedListCycleStart() {
    }

    /**
     * Алгоритм Флойда: сначала находим точку встречи медленного и быстрого указателя,
     * затем второй проход от начала списка находит узел, с которого начинается цикл.
     * head может быть null — это означает пустой список.
     */
    public static Node findCycleStart(Node head) {
        // ---8<--- solution
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                Node pointer = head;
                while (pointer != slow) {
                    pointer = pointer.next;
                    slow = slow.next;
                }
                return pointer;
            }
        }
        return null;
        // --->8--- solution
    }

    public static final class Node {
        public final int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }
}
