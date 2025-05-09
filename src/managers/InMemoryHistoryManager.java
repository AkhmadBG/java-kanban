package managers;

import model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> history = new LinkedHashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;
        Node<Task> node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
            history.remove(task.getId());
        }
        Node<Task> newNode = linkLast(task);
        history.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return history.values().stream()
                .map(node -> node.data)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(int id) {
        Node<Task> node = history.get(id);
        if (node != null) {
            removeNode(node);
            history.remove(id);
        }
    }

    @Override
    public void removeAllTasks() {
        history.clear();
        clear();
    }

    public static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        public Node(E data) {
            this.data = data;
        }
    }

    private static Node<Task> head;
    private static Node<Task> tail;

    public static Node<Task> linkLast(Task data) {
        Node<Task> newNode = new Node<>(data);
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

    public static void removeNode(Node<Task> node) {
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

    public static void clear() {
        for (Node<Task> x = head; x != null; ) {
            Node<Task> next = x.next;
            x.data = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        head = tail = null;
    }
}
