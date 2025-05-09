import managers.FileBackedTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static enums.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File file;
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    void setup() throws IOException {
        file = File.createTempFile("taskmanager", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @AfterEach
    void deleteFile() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void shouldSaveAndLoadTask() {
        Task task = fileBackedTaskManager.createTask("Задача", "Описание");
        task.setTaskStatus(IN_PROGRESS);
        fileBackedTaskManager.updateTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task loadedTask = loadedManager.getTask(task.getId());

        assertNotNull(loadedTask);
        assertEquals("Задача", loadedTask.getName());
        assertEquals("Описание", loadedTask.getDescription());
        assertEquals(IN_PROGRESS, loadedTask.getTaskStatus());
    }

    @Test
    void shouldSaveAndLoadEpicWithSubtask() {
        Epic epic = fileBackedTaskManager.createEpic("Эпик", "Описание эпика");
        SubTask subTask = fileBackedTaskManager.createSubTask("Подзадача", "Описание подзадачи", epic.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Epic loadedEpic = loadedManager.getEpic(epic.getId());
        List<SubTask> loadedSubTasks = loadedManager.getAllSubTasksInEpic(epic.getId());

        assertNotNull(loadedEpic);
        assertEquals("Эпик", loadedEpic.getName());
        assertTrue(loadedEpic.getSubTasksIds().contains(subTask.getId()));

        assertEquals(1, loadedSubTasks.size());
        assertEquals("Подзадача", loadedSubTasks.get(0).getName());
    }

    @Test
    void shouldUpdateTask() {
        Task task = fileBackedTaskManager.createTask("Задача", "Описание");
        task.setName("Обновление названия задачи");
        task.setDescription("Обновление описания");
        task.setTaskStatus(DONE);
        fileBackedTaskManager.updateTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task updated = loadedManager.getTask(task.getId());

        assertEquals("Обновление названия задачи", updated.getName());
        assertEquals("Обновление описания", updated.getDescription());
        assertEquals(DONE, updated.getTaskStatus());
    }

    @Test
    void shouldUpdateSubTask() {
        Epic epic = fileBackedTaskManager.createEpic("Эпик", "Описание");
        SubTask subTask = fileBackedTaskManager.createSubTask("Подзадача", "Описание", epic.getId());

        subTask.setName("Обновление названия подзадачи");
        subTask.setDescription("Обновление описания");
        subTask.setTaskStatus(IN_PROGRESS);
        fileBackedTaskManager.updateSubTask(subTask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        SubTask updated = loadedManager.getSubTask(subTask.getId());

        assertEquals("Обновление названия подзадачи", updated.getName());
        assertEquals(IN_PROGRESS, updated.getTaskStatus());
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = fileBackedTaskManager.createEpic("Эпик", "Описание");
        epic.setName("Обновление названия");
        epic.setDescription("Обновление описания");
        fileBackedTaskManager.updateEpic(epic);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Epic updated = loadedManager.getEpic(epic.getId());

        assertEquals("Обновление названия", updated.getName());
        assertEquals("Обновление описания", updated.getDescription());
    }
}
