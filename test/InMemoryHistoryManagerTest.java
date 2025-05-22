import managers.impl.InMemoryHistoryManager;
import managers.impl.InMemoryTaskManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
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
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void shouldRemoveOldestWatchingTask() {
        Task task1 = taskManager.createTask("Задача 1", "Описание задачи 1");
        task1.setDuration(Duration.parse("PT30M"));
        task1.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Task task2 = taskManager.createTask("Задача 2", "Описание задачи 2");
        task2.setDuration(Duration.parse("PT30M"));
        task2.setStartTime(LocalDateTime.of(2002, FEBRUARY, 2, 2, 2));
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getTask(2);

        List<Task> list = taskManager.getHistory();

        assertEquals(2, list.size());
        assertEquals(task1, list.get(0));
        assertEquals(task2, list.get(1));
    }

    @Test
    void shouldReturnEmptyHistoryInitially() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveAllTasks() {
        Task task1 = taskManager.createTask("Задача 1", "Описание задачи 1");
        task1.setDuration(Duration.parse("PT30M"));
        task1.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Task task2 = taskManager.createTask("Задача 2", "Описание задачи 2");
        task2.setDuration(Duration.parse("PT30M"));
        task2.setStartTime(LocalDateTime.of(2002, FEBRUARY, 2, 2, 2));

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.removeAllTasks();
        assertEquals(0, historyManager.getHistory().size());
    }

}
