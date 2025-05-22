import managers.impl.FileBackedTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static enums.TaskStatus.*;
import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {

    private File file;

    @BeforeEach
    void setup() throws IOException {
        file = File.createTempFile("taskmanager", ".csv");
        taskManager = new FileBackedTaskManager(file);
    }

    @AfterEach
    void deleteFile() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return (FileBackedTaskManager) taskManager;
    }

    @Test
    void shouldSaveAndLoadTask() {
        Task task = new Task("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        task.setTaskStatus(IN_PROGRESS);
        taskManager.addNewTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task loadedTask = loadedManager.getTask(task.getId());

        assertNotNull(loadedTask);
        assertEquals("Задача 1", loadedTask.getName());
        assertEquals("Описание задачи 1", loadedTask.getDescription());
        assertEquals(Duration.parse("PT30M"), task.getDuration());
        assertEquals(LocalDateTime.of(2001, JANUARY, 1, 1, 1), task.getStartTime());
        assertEquals(LocalDateTime.of(2001, JANUARY, 1, 1, 31), task.getEndTime());
        assertEquals(IN_PROGRESS, task.getTaskStatus());
    }

    @Test
    void shouldSaveAndLoadEpicWithSubtask() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subTask.setDuration(Duration.parse("PT30M"));
        subTask.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        taskManager.addNewSubTask(subTask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Epic loadedEpic = loadedManager.getEpic(epic.getId());
        List<SubTask> loadedSubTasks = loadedManager.getAllSubTasksInEpic(loadedEpic.getId());

        assertNotNull(loadedEpic);
        assertEquals("Эпик", loadedEpic.getName());
        assertTrue(loadedEpic.getSubTasksIds().contains(subTask.getId()));
        assertEquals(1, loadedSubTasks.size());
        assertEquals("Подзадача 1", loadedSubTasks.get(0).getName());
    }

}
