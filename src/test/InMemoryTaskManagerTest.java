import managers.InMemoryTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldCreateTask() {
        Task task = taskManager.createTask("Задача  1", "Описание 1");

        assertNotNull(task);
        assertEquals("Задача  1", task.getName());
        assertEquals("Описание 1", task.getDescription());
    }

    @Test
    void shouldCreateEpic() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");

        assertNotNull(epic);
        assertEquals("Эпик 1", epic.getName());
        assertEquals("Описание эпика", epic.getDescription());
    }

    @Test
    void shouldCreateSubTaskAndLinkToEpic() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи", epic.getId());

        assertNotNull(subTask);
        assertEquals("Подзадача 1", subTask.getName());
        assertEquals(epic.getId(), subTask.getEpicId());
        assertTrue(epic.getSubTasksIds().contains(subTask.getId()));
    }

    @Test
    void shouldReturnTaskById() {
        Task task = taskManager.createTask("Задача 1", "Описание 1");
        Task fetchedTask = taskManager.getTask(task.getId());

        assertNotNull(fetchedTask);
        assertEquals(task, fetchedTask);
    }

    @Test
    void shouldReturnEpicById() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        Epic fetchedEpic = (Epic) taskManager.getEpic(epic.getId());

        assertNotNull(fetchedEpic);
        assertEquals(epic, fetchedEpic);
    }

    @Test
    void shouldReturnSubTaskById() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи", epic.getId());
        SubTask fetchedSubTask = (SubTask) taskManager.getSubTask(subTask.getId());

        assertNotNull(fetchedSubTask);
        assertEquals(subTask, fetchedSubTask);
    }

    @Test
    void shouldDeleteTask() {
        Task task = taskManager.createTask("Задача 1", "Описание 1");
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    void shouldDeleteEpicAndItsSubTasks() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи", epic.getId());
        taskManager.deleteEpic(epic.getId());

        assertNull(taskManager.getEpic(epic.getId()));
        assertNull(taskManager.getSubTask(subTask.getId()));
    }

    @Test
    void shouldDeleteAllTasks() {
        taskManager.createTask("Задача 1", "Описание 1");
        taskManager.createEpic("Эпик 1", "Описание эпика");
        taskManager.deleteAllTasks();

        assertTrue(taskManager.getTasks().isEmpty());
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubTasks().isEmpty());
    }
}
