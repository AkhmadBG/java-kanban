import exceptions.CheckIntersectionWithAllTasksException;
import managers.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static enums.TaskStatus.*;
import static enums.TaskStatus.DONE;
import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    void shouldCreateTask() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));

        assertNotNull(task);
        assertEquals("Задача 1", task.getName());
        assertEquals("Описание задачи 1", task.getDescription());

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
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));

        assertNotNull(subTask);
        assertEquals("Подзадача 1", subTask.getName());
        assertEquals(epic.getId(), subTask.getEpicId());
        assertTrue(epic.getSubTasksIds().contains(subTask.getId()));
    }

    @Test
    void shouldReturnTaskById() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Task fetchedTask = taskManager.getTask(task.getId());

        assertNotNull(fetchedTask);
        assertEquals(task, fetchedTask);
    }

    @Test
    void shouldReturnEpicById() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        Epic fetchedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(fetchedEpic);
        assertEquals(epic, fetchedEpic);
    }

    @Test
    void shouldReturnSubTaskById() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        SubTask fetchedSubTask = taskManager.getSubTask(subTask.getId());

        assertNotNull(fetchedSubTask);
        assertEquals(subTask, fetchedSubTask);
    }

    @Test
    void shouldUpdateTask() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Task updatedTask = new Task("Обновление названия задачи", "Обновление описания");
        updatedTask.setDuration(Duration.parse("PT90M"));
        updatedTask.setStartTime(LocalDateTime.of(2002, FEBRUARY, 2, 2, 2));
        updatedTask.setTaskStatus(DONE);
        updatedTask.setId(1);
        taskManager.updateTask(updatedTask);

        Task updated = taskManager.getTask(task.getId());
        assertEquals("Обновление названия задачи", updated.getName());
        assertEquals("Обновление описания", updated.getDescription());
        assertEquals(Duration.ofMinutes(90), updated.getDuration());
        assertEquals(LocalDateTime.of(2002, FEBRUARY, 2, 2, 2), updated.getStartTime());
        assertEquals(DONE, updated.getTaskStatus());
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = taskManager.createEpic("Эпик", "Описание");
        Epic updatedEpic = taskManager.createEpic("Обновление названия", "Обновление описания");
        updatedEpic.setId(1);
        taskManager.updateEpic(updatedEpic);

        Epic updated = taskManager.getEpic(epic.getId());
        assertEquals("Обновление названия", updated.getName());
        assertEquals("Обновление описания", updated.getDescription());
    }

    @Test
    void shouldUpdateSubTask() {
        Epic epic = taskManager.createEpic("Эпик", "Описание");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        SubTask updatedSubTask = new SubTask("Обновление названия подзадачи 1", "Обновление описание подзадачи 1", epic.getId());
        updatedSubTask.setDuration(Duration.parse("PT90M"));
        updatedSubTask.setStartTime(LocalDateTime.of(2000, JANUARY, 1, 1, 1));
        updatedSubTask.setTaskStatus(IN_PROGRESS);
        updatedSubTask.setId(2);
        taskManager.updateSubTask(updatedSubTask);

        SubTask updated = taskManager.getSubTask(subTask.getId());
        assertEquals("Обновление названия подзадачи 1", updated.getName());
        assertEquals("Обновление описание подзадачи 1", updated.getDescription());
        assertEquals(Duration.ofMinutes(90), updated.getDuration());
        assertEquals(LocalDateTime.of(2000, JANUARY, 1, 1, 1), updated.getStartTime());
        assertEquals(IN_PROGRESS, updated.getTaskStatus());
    }

    @Test
    void shouldDeleteTask() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    void shouldDeleteEpicAndItsSubTasks() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        taskManager.deleteEpic(epic.getId());

        assertNull(taskManager.getEpic(epic.getId()));
        assertNull(taskManager.getSubTask(subTask.getId()));
    }

    @Test
    void shouldDeleteSubTask() {
        Epic epic = taskManager.createEpic("Эпик", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        taskManager.deleteSubTask(subTask.getId());
        assertNull(taskManager.getSubTask(subTask.getId()));
    }

    @Test
    void shouldDeleteAllTasks() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Epic epic = taskManager.createEpic("Эпик", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));

        taskManager.deleteAllTasks();

        assertTrue(taskManager.getTasks().isEmpty());
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubTasks().isEmpty());
    }

    @Test
    void shouldSetCorrectEpicStatus() {
        Epic epic = taskManager.createEpic("Эпик 1", "Описание эпика");
        SubTask subTask1 = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask1.setDuration(Duration.parse("PT30M"));
        subTask1.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        SubTask subTask2 = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask2.setDuration(Duration.parse("PT30M"));
        subTask2.setStartTime(LocalDateTime.of(2000, JANUARY, 1, 1, 1));

        assertEquals(NEW, epic.getTaskStatus());

        subTask1.setTaskStatus(IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        assertEquals(IN_PROGRESS, epic.getTaskStatus());

        subTask2.setTaskStatus(IN_PROGRESS);
        taskManager.updateSubTask(subTask2);
        assertEquals(IN_PROGRESS, epic.getTaskStatus());

        subTask1.setTaskStatus(DONE);
        taskManager.updateSubTask(subTask1);
        assertEquals(IN_PROGRESS, epic.getTaskStatus());

        subTask2.setTaskStatus(DONE);
        taskManager.updateSubTask(subTask2);
        assertEquals(DONE, epic.getTaskStatus());
    }

    @Test
    void shouldReturnAllTasks() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Epic epic = taskManager.createEpic("Эпик", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));

        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(3, allTasks.size());
        assertEquals(task, allTasks.get(0));
        assertEquals(epic, allTasks.get(1));
        assertEquals(subTask, allTasks.get(2));

    }

    @Test
    void shouldReturnPrioritisedTasks() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));

        Epic epic = taskManager.createEpic("Эпик", "Описание эпика");

        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));

        Set<Task> prioritisedTasks2 = taskManager.getPrioritisedTasks();

        assertTrue(prioritisedTasks2.contains(task));
        assertFalse(prioritisedTasks2.contains(epic));
    }

    @Test
    void shouldAddNewTask() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        taskManager.addNewTask(task);
        assertEquals(task, taskManager.getTask(1));
    }

    @Test
    void shouldAddNewEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        taskManager.addNewEpic(epic);
        assertEquals(epic, taskManager.getEpic(1));
    }

    @Test
    void shouldAddNewSubTask() {
        Epic epic = taskManager.createEpic("Эпик", "Описание эпика");
        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        taskManager.addNewSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTask(2));
    }

    @Test
    void shouldReturnHistory() {
        Task task = taskManager.createTask("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Epic epic = taskManager.createEpic("Эпик", "Описание эпика");
        SubTask subTask = taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));

        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubTask(3);

        List<Task> history = taskManager.getHistory();
        assertTrue(history.contains(task));
        assertTrue(history.contains(epic));
        assertTrue(history.contains(subTask));
    }

    @Test
    void shouldThrowExceptionIfTasksOverlap() {
        Task task1 = taskManager.createTask("Задача 1", "Описание задачи 1");
        task1.setDuration(Duration.parse("PT30M"));
        task1.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        taskManager.updateTask(task1);

        Task task2 = taskManager.createTask("Задача 2", "Описание задачи 2");
        task2.setDuration(Duration.parse("PT30M"));
        task2.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        task2.setId(1);

        Task task3 = taskManager.createTask("Задача 3", "Описание задачи 3");
        task3.setDuration(Duration.parse("PT30M"));
        task3.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        task3.setId(2);

        assertThrows(CheckIntersectionWithAllTasksException.class, () -> taskManager.updateTask(task2));
        assertThrows(CheckIntersectionWithAllTasksException.class, () -> taskManager.addNewTask(task3));
    }

    @Test
    void shouldContainsTasksInPrioritisedTasks() {
        Task task1 = taskManager.createTask("Задача 1", "Описание задачи 1");
        task1.setDuration(Duration.parse("PT30M"));
        task1.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        Task task2 = taskManager.createTask("Задача 1", "Описание задачи 1");
        task2.setDuration(Duration.parse("PT30M"));
        task2.setStartTime(LocalDateTime.of(2002, FEBRUARY, 2, 2, 2));

        Set<Task> prioritisedTasks = taskManager.getPrioritisedTasks();
        assertTrue(prioritisedTasks.contains(task1));
        assertTrue(prioritisedTasks.contains(task2));
    }

}