package managers;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    Task createTask(String name, String description);

    Epic createEpic(String name, String description);

    SubTask createSubTask(String name, String description, int epicId);

    Task getTask(int taskId);

    Task getEpic(int epicId);

    Task getSubTask(int subTaskId);

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasksInEpic(int epicId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubTask(int subTasksId);

    void deleteAllTasks();

    void addNewTask(Task task);

    void addNewEpic(Epic epic);

    void addNewSubTask(SubTask subTask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<Task> getHistory();
}
