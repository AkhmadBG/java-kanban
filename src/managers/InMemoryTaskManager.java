package managers;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int identifier;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task createTask(String name, String description) {
        Task task = new Task(name, description);
        task.setId(++identifier);
        tasks.put(identifier, task);
        return task;
    }

    @Override
    public Epic createEpic(String name, String description) {
        Epic epic = new Epic(name, description);
        epic.setId(++identifier);
        epics.put(identifier, epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(String name, String description, int epicId) {
        SubTask subTask = new SubTask(name, description, epicId);
        subTask.setId(++identifier);
        subTasks.put(identifier, subTask);
        epics.get(epicId).addSubTaskId(subTask.getId());
        return subTask;
    }

    @Override
    public Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            Task task = tasks.get(taskId);
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Task getEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            historyManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Task getSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            SubTask subTask = subTasks.get(subTaskId);
            historyManager.add(subTask);
            return subTask;
        }
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subTasks);
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasksInEpic(int epicId) {
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
            return new ArrayList<>(allSubTasksInEpic.values());
        }
        return null;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTasks.get(subTask.getId()).getEpicId());
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
            for (Integer subTaskId : subTaskIds) {
                subTasks.remove(subTaskId);
            }
            epics.remove(epicId);
        }
    }

    @Override
    public void deleteSubTask(int subTasksId) {
        if (subTasks.containsKey(subTasksId)) {
            Epic epic = epics.get(subTasks.get(subTasksId).getEpicId());
            epic.removeSubTaskId(subTasksId);
            updateEpicStatus(epic);
            subTasks.remove(subTasksId);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void addNewTask(Task task) {
        task.setId(++identifier);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setId(++identifier);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        subTask.setId(++identifier);
        epics.get(subTask.getEpicId()).addSubTaskId(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return identifier == that.identifier
                && Objects.equals(tasks, that.tasks)
                && Objects.equals(epics, that.epics)
                && Objects.equals(subTasks, that.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, tasks, epics, subTasks);
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

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }
}
