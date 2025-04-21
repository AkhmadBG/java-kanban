import managers.CustomLinkedList;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomLinkedListTest {

    private CustomLinkedList<Task> customLinkedList;

    @BeforeEach
    void setUp() {
        customLinkedList = new CustomLinkedList<>();
    }

    @Test
    void shouldAddTaskToEndListTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);

        List<Task> list = customLinkedList.getTasks();

        assertEquals(2, list.size());
        assertEquals(task1, list.get(0));
        assertEquals(task2, list.get(1));
    }

    @Test
    void shouldReturnEmptyListTasks(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);

        customLinkedList.clear();

        List<Task> list = customLinkedList.getTasks();

        assertEquals(0, list.size());
    }

    @Test
    void shouldRemoveTaskFromLinkedListAfterDeleteTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Task task3 = new Task("Задача 3", "Описание задачи 3");

        customLinkedList.linkLast(task1);
        CustomLinkedList.Node<Task> node2 = customLinkedList.linkLast(task2);
        customLinkedList.linkLast(task3);
        customLinkedList.removeNode(node2);

        List<Task> list = customLinkedList.getTasks();

        assertEquals(2, list.size());
        assertEquals(task1, list.get(0));
        assertEquals(task3, list.get(1));
    }


}