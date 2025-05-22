package managers;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {

    Task createTask(String name, String description);

    Epic createEpic(String name, String description);

    SubTask createSubTask(String name, String description, int epicId);

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubTask(int subTaskId);

    List<Task> getAllTasks();

    Set<Task> getPrioritisedTasks();

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

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, SubTask> getSubTasks();

    List<Task> getHistory();

}
