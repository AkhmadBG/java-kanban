package managers;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<T extends Task> {

    public static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        public Node(E data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;

    public Node<T> linkLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        return newNode;
    }

    public List<T> getTasks() {
        List<T> tasks = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    public void removeNode(Node<T> node) {
        if (node == null) return;
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    public void clear() {
        for (CustomLinkedList.Node<T> x = head; x != null; ) {
            CustomLinkedList.Node<T> next = x.next;
            x.data = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        head = tail = null;
    }
}
