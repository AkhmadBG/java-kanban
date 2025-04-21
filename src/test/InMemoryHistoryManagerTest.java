import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldAddTaskToHistory() {
        Task task = taskManager.createTask("Задача  1", "Описание 1");
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void shouldRemoveOldestWatchingTask() {
        Task task1 = taskManager.createTask("Задача 1", "Описание задачи 1");
        Task task2 = taskManager.createTask("Задача 2", "Описание задачи 2");
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(1);
        List<Task> list = taskManager.getHistory();

        assertEquals(task1, list.get(1));
        assertEquals(task2, list.get(0));
    }

    @Test
    void shouldReturnEmptyHistoryInitially() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveAllTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.removeAllTasks();
        assertEquals(0, historyManager.getHistory().size());
    }
}
