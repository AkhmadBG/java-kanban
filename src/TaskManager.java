import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TaskManager {

    private int identifier;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public Task createTask(String name, String description) {
        Task task = new Task(name, description);
        task.setId(++identifier);
        tasks.put(identifier, task);
        return task;
    }

    public Epic createEpic(String name, String description) {
        Epic epic = new Epic(name, description);
        epic.setId(++identifier);
        epics.put(identifier, epic);
        return epic;
    }

    public SubTask createSubTask(String name, String description, int epicId) {
        SubTask subTask = new SubTask(name, description, epicId);
        subTask.setId(++identifier);
        subTasks.put(identifier, subTask);
        epics.get(epicId).addSubTaskId(subTask.getId());
        return subTask;
    }

    public Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        }
        return null;
    }

    public Task getEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId);
        }
        return null;
    }

    public Task getSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            return subTasks.get(subTaskId);
        }
        return null;
    }

    public Map<Integer, Task> getAllTasks() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subTasks);
        return allTasks;
    }

    public Map<Integer, SubTask> getAllSubTasksInEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Map<Integer, SubTask> allSubTasksInEpic = new HashMap<>();
            ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
            for (Integer subTaskId : subTaskIds) {
                for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                    if (subTaskId.equals(entry.getKey())) {
                        allSubTasksInEpic.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            return allSubTasksInEpic;
        }
        return null;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTasks.get(subTask.getId()).getEpicId());
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subTaskIdsInEpic = epic.getSubTasksIds();

        boolean flagNew = true;
        boolean flagDone = true;

        for (Integer subTaskIdInEpic : subTaskIdsInEpic) {
            if (!subTasks.get(subTaskIdInEpic).getTaskStatus().equals(TaskStatus.NEW)) {
                flagNew = false;
                break;
            }
        }

        for (Integer subTaskIdInEpic : subTaskIdsInEpic) {
            if (!subTasks.get(subTaskIdInEpic).getTaskStatus().equals(TaskStatus.DONE)) {
                flagDone = false;
                break;
            }
        }

        if (flagNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (flagDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
    }

    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
            for (Integer subTaskId : subTaskIds) {
                subTasks.remove(subTaskId);
            }
            epics.remove(epicId);
        }
    }

    public void deleteSubTask(int subTasksId) {
        if (subTasks.containsKey(subTasksId)) {
            Epic epic = epics.get(subTasks.get(subTasksId).getEpicId());
            epic.removeSubTaskId(subTasksId);
            updateEpicStatus(epic);
            subTasks.remove(subTasksId);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    public void addNewTask(Task task) {
        task.setId(++identifier);
        tasks.put(task.getId(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setId(++identifier);
        epics.put(epic.getId(), epic);
    }

    public void addNewSubTask(SubTask subTask) {
        subTask.setId(++identifier);
        epics.get(subTask.getEpicId()).addSubTaskId(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public void setEpics(Map<Integer, Epic> epics) {
        this.epics = epics;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskManager that = (TaskManager) o;
        return identifier == that.identifier && Objects.equals(tasks, that.tasks) && Objects.equals(epics, that.epics) && Objects.equals(subTasks, that.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, tasks, epics, subTasks);
    }
}
