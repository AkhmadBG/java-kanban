package managers;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();
    private final Map<Integer, CustomLinkedList.Node<Task>> history = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;
        CustomLinkedList.Node<Task> node = history.get(task.getId());
        if (node != null) {
            customLinkedList.removeNode(node);
        }
        CustomLinkedList.Node<Task> newNode = customLinkedList.linkLast(task);
        history.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        CustomLinkedList.Node<Task> node = history.get(id);
        if (node != null) {
            customLinkedList.removeNode(node);
            history.remove(id);
        }
    }

    @Override
    public void removeAllTasks() {
        history.clear();
        customLinkedList.clear();
    }
}
