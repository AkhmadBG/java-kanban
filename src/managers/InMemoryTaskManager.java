package managers;

import enums.TaskStatus;
import enums.TaskType;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int identifier;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task createTask(TaskType type, String name, String description) {
        Task task = new Task(type, name, description);
        task.setId(++identifier);
        tasks.put(identifier, task);
        return task;
    }

    @Override
    public Epic createEpic(TaskType type, String name, String description) {
        Epic epic = new Epic(type, name, description);
        epic.setId(++identifier);
        epics.put(identifier, epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(TaskType type, String name, String description, int epicId) {
        SubTask subTask = new SubTask(type, name, description, epicId);
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
    public Epic getEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            historyManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
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
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
        return epic;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTasks.get(subTask.getId()).getEpicId());
            updateEpicStatus(epic);
        }
        return subTask;
    }

    @Override
    public void deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            historyManager.remove(taskId);
        }
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
            for (Integer subTaskId : subTaskIds) {
                subTasks.remove(subTaskId);
                historyManager.remove(subTaskId);
            }
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubTask(int subTasksId) {
        if (subTasks.containsKey(subTasksId)) {
            Epic epic = epics.get(subTasks.get(subTasksId).getEpicId());
            epic.removeSubTaskId(subTasksId);
            updateEpicStatus(epic);
            subTasks.remove(subTasksId);
            historyManager.remove(subTasksId);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        historyManager.removeAllTasks();
    }

    @Override
    public Task addNewTask(Task task) {
        task.setId(++identifier);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        epic.setId(++identifier);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addNewSubTask(SubTask subTask) {
        subTask.setId(++identifier);
        epics.get(subTask.getEpicId()).addSubTaskId(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        return subTask;
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
