package trainer.algorithms.l3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LinkedListCycleStartTest {

    @Test
    void returnsNullForAcyclicList() {
        LinkedListCycleStart.Node head = chain(1, 2, 3);
        assertThat(LinkedListCycleStart.findCycleStart(head)).isNull();
    }

    @Test
    void returnsNullForEmptyList() {
        assertThat(LinkedListCycleStart.findCycleStart(null)).isNull();
    }

    @Test
    void findsStartOfCycleInMiddle() {
        LinkedListCycleStart.Node head = chain(1, 2, 3, 4, 5);
        LinkedListCycleStart.Node cycleStart = nodeAt(head, 2); // значение 3
        nodeAt(head, 4).next = cycleStart;

        assertThat(LinkedListCycleStart.findCycleStart(head)).isSameAs(cycleStart);
    }

    @Test
    void findsSelfLoopAsCycleStart() {
        LinkedListCycleStart.Node head = new LinkedListCycleStart.Node(1);
        head.next = head;

        assertThat(LinkedListCycleStart.findCycleStart(head)).isSameAs(head);
    }

    private static LinkedListCycleStart.Node chain(int... values) {
        LinkedListCycleStart.Node head = null;
        LinkedListCycleStart.Node tail = null;
        for (int value : values) {
            LinkedListCycleStart.Node node = new LinkedListCycleStart.Node(value);
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
        }
        return head;
    }

    private static LinkedListCycleStart.Node nodeAt(LinkedListCycleStart.Node head, int index) {
        LinkedListCycleStart.Node node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }
}
