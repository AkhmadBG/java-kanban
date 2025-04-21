import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {

    @Test
    void shouldGetDefaultInMemoryTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");

        assertEquals(task, taskManager.getTask(1), "Должен вернуть созданную задачу");
    }

    @Test
    void shouldGetDefaultInMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика 1");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", 2);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);

        assertEquals(task, taskManager.getTask(1), "Должен вернуть созданную задачу");
        assertEquals(epic, taskManager.getEpic(2), "Должен вернуть созданный эпик");
        assertEquals(subTask, taskManager.getSubTask(3), "Должен вернуть созданную подзадачу");
    }
}