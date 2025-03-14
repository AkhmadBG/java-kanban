import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TaskManager {

    private int identifier;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public void createTask(String name, String description) {
        tasks.put(++identifier, new Task(identifier, name, description));
    }

    public void createEpic(String name, String description) {
        epics.put(++identifier, new Epic(identifier, name, description));
    }

    public void createSubTask(String name, String description, int epicId) {
        SubTask subTask = new SubTask(++identifier, name, description, epicId);
        subTasks.put(identifier, subTask);
        epics.get(epicId).addSubTaskId(subTask.getId());
    }

    public Task readTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        }
        return null;
    }

    public Task readEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId);
        }
        return null;
    }

    public Task readSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            return subTasks.get(subTaskId);
        }
        return null;
    }

    public Map<Integer, Task> readAllTasks() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subTasks);
        return allTasks;
    }

    public Map<Integer, SubTask> readAllSubTasksInEpic(int epicId) {
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

    public void updateTask(int taskId, String name, String description, TaskStatus taskStatus) {
        if (tasks.containsKey(taskId)) {
            Task task = tasks.get(taskId);
            task.setName(name);
            task.setDescription(description);
            task.setTaskStatus(taskStatus);
        }
    }

    public void updateEpic(int epicId, String name, String description) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            epic.setName(name);
            epic.setDescription(description);
        }
    }

    public void updateSubTask(int subTaskId, String name, String description, TaskStatus taskStatus) {
        if (subTasks.containsKey(subTaskId)) {
            SubTask subTask = subTasks.get(subTaskId);
            subTask.setName(name);
            subTask.setDescription(description);
            subTask.setTaskStatus(taskStatus);
            ArrayList<Integer> subTaskIdsInEpic = epics.get(subTask.getEpicId()).getSubTasksIds();
            boolean flag = true;
            for (Integer subTaskIdInEpic : subTaskIdsInEpic) {
                if (!subTasks.get(subTaskIdInEpic).getTaskStatus().equals(TaskStatus.DONE)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                epics.get(subTasks.get(subTaskId).getEpicId()).setTaskStatus(TaskStatus.DONE);
            } else {
                epics.get(subTasks.get(subTaskId).getEpicId()).setTaskStatus(TaskStatus.IN_PROGRESS);
            }
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
            for (Integer subTaskId: subTaskIds) {
                subTasks.remove(subTaskId);
            }
            epics.remove(epicId);
        }
    }

    public void deleteSubTask(int subTasksId) {
        if (subTasks.containsKey(subTasksId)) {
            epics.get(subTasks.get(subTasksId).getEpicId()).removeSubTaskId(subTasksId);
            subTasks.remove(subTasksId);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
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
