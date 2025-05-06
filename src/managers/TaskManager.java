package managers;

import enums.TaskType;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {

    Task createTask(TaskType type, String name, String description);

    Epic createEpic(TaskType type, String name, String description);

    SubTask createSubTask(TaskType type, String name, String description, int epicId);

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubTask(int subTaskId);

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasksInEpic(int epicId);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    SubTask updateSubTask(SubTask subTask);

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubTask(int subTasksId);

    void deleteAllTasks();

    Task addNewTask(Task task);

    Epic addNewEpic(Epic epic);

    SubTask addNewSubTask(SubTask subTask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<Task> getHistory();

}
