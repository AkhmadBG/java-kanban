import managers.FileBackedTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static enums.TaskStatus.*;
import static enums.TaskType.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        Task task = fileBackedTaskManager.createTask(TASK_TYPE, "Задача", "Описание");
        task.setTaskStatus(IN_PROGRESS);
        fileBackedTaskManager.updateTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task loadedTask = loadedManager.getTask(task.getId());

        assertThat(loadedTask).isNotNull();
        assertThat(loadedTask.getName()).isEqualTo("Задача");
        assertThat(loadedTask.getDescription()).isEqualTo("Описание");
        assertThat(loadedTask.getTaskStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    void shouldSaveAndLoadEpicWithSubtask() {
        Epic epic = fileBackedTaskManager.createEpic(EPIC_TYPE, "Эпик", "Описание эпика");
        SubTask subTask = fileBackedTaskManager.createSubTask(SUBTASK_TYPE, "Подзадача", "Описание подзадачи", epic.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Epic loadedEpic = loadedManager.getEpic(epic.getId());
        List<SubTask> loadedSubTasks = loadedManager.getAllSubTasksInEpic(1);

        assertThat(loadedEpic).isNotNull();
        assertThat(loadedEpic.getName()).isEqualTo("Эпик");
        assertThat(loadedEpic.getSubTasksIds()).contains(subTask.getId());

        assertThat(loadedSubTasks).hasSize(1);
        assertThat(loadedSubTasks.get(0).getName()).isEqualTo("Подзадача");
    }

    @Test
    void shouldUpdateTask() {
        Task task = fileBackedTaskManager.createTask(TASK_TYPE, "Задача", "Описание");
        task.setName("Обновление названия задачи");
        task.setDescription("Обновление описания");
        task.setTaskStatus(DONE);
        fileBackedTaskManager.updateTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task updated = loadedManager.getTask(task.getId());

        assertThat(updated.getName()).isEqualTo("Обновление названия задачи");
        assertThat(updated.getDescription()).isEqualTo("Обновление описания");
        assertThat(updated.getTaskStatus()).isEqualTo(DONE);
    }

    @Test
    void shouldUpdateSubTask() {
        Epic epic = fileBackedTaskManager.createEpic(EPIC_TYPE, "Эпик", "Описание");
        SubTask subTask = fileBackedTaskManager.createSubTask(SUBTASK_TYPE, "Подзадача", "Описание", epic.getId());

        subTask.setName("Обновление названия подзадачи");
        subTask.setDescription("Обновление описания");
        subTask.setTaskStatus(IN_PROGRESS);
        fileBackedTaskManager.updateSubTask(subTask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        SubTask updated = loadedManager.getSubTask(subTask.getId());

        assertThat(updated.getName()).isEqualTo("Обновление названия подзадачи");
        assertThat(updated.getTaskStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = fileBackedTaskManager.createEpic(EPIC_TYPE, "Эпик", "Описание");
        epic.setName("Обновление названия");
        epic.setDescription("Обновление описания");
        fileBackedTaskManager.updateEpic(epic);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Epic updated = loadedManager.getEpic(epic.getId());

        assertThat(updated.getName()).isEqualTo("Обновление названия");
        assertThat(updated.getDescription()).isEqualTo("Обновление описания");
    }

}
