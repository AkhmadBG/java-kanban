import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {

    @Test
    void shouldGetDefaultInMemoryTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        taskManager.addNewTask(task);

        assertEquals(task, taskManager.getTask(1), "Должен вернуть созданную задачу");
    }

    @Test
    void shouldGetDefaultInMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        taskManager.addNewTask(task);

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addNewEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1", 2);
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        taskManager.addNewSubTask(subTask);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);

        assertEquals(task, taskManager.getTask(1), "Должен вернуть созданную задачу");
        assertEquals(epic, taskManager.getEpic(2), "Должен вернуть созданный эпик");
        assertEquals(subTask, taskManager.getSubTask(3), "Должен вернуть созданную подзадачу");
    }

}