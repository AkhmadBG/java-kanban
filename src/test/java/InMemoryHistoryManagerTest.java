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
    void shouldNotExceedMaxHistorySize() {
        for (int i = 1; i <= 11; i++) {
            historyManager.add(taskManager.createTask("Задача " + i, "Описание " + i));
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size());
        assertEquals("Задача 2", history.get(0).getName());
    }

    @Test
    void shouldRemoveOldestTaskWhenLimitExceeded() {
        for (int i = 1; i <= 10; i++) {
            historyManager.add(taskManager.createTask("Задача " + i, "Описание " + i));
        }
        Task oldestTask = historyManager.getHistory().get(0);
        Task newTask = new Task("Задача  1", "Описание 1");
        historyManager.add(newTask);

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size());
        assertFalse(history.contains(oldestTask));
        assertEquals(newTask, history.get(history.size() - 1));
    }

    @Test
    void shouldReturnEmptyHistoryInitially() {
        assertTrue(historyManager.getHistory().isEmpty());
    }
}
