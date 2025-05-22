package managers.impl;

import exceptions.CheckIntersectionWithAllTasksException;
import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static enums.TaskStatus.*;

public class InMemoryTaskManager implements TaskManager {

    private int identifier;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public Set<Task> allTasksSortList = new TreeSet<>(Comparator
            .comparing(Task::getStartTime));

    @Override
    public Task createTask(String name, String description) {
        Task task = new Task(name, description);
        task.setId(++identifier);
        if (checkIntersectionWithAllTasks(task)) return null;
        tasks.put(identifier, task);
        if (task.getStartTime() != null) {
            allTasksSortList.add(task);
        }
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
        if (checkIntersectionWithAllTasks(subTask)) return null;
        subTasks.put(identifier, subTask);
        Epic epic = epics.get(epicId);
        epic.addSubTaskId(subTask.getId());
        updateEpicStatus(epic);
        updateEpicsDurationStartEndDate(epic);
        if (subTask.getStartTime() != null) {
            allTasksSortList.add(subTask);
        }
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
    public Set<Task> getPrioritisedTasks() {
        return allTasksSortList;
    }

    @Override
    public List<SubTask> getAllSubTasksInEpic(int epicId) {
        if (!epics.containsKey(epicId)) return null;

        return epics.get(epicId).getSubTasksIds().stream()
                .map(subTasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) return null;
        allTasksSortList.remove(tasks.get(task.getId()));
        tasks.remove(task.getId());
        if (checkIntersectionWithAllTasks(task)) return null;
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            allTasksSortList.add(task);
        }
        return task;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        if (epics.containsKey(newEpic.getId())) {
            epic.setName(newEpic.getName());
            epic.setDescription(newEpic.getDescription());
        }
        return epic;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) return null;
        allTasksSortList.remove(subTasks.get(subTask.getId()));
        subTasks.remove(subTask.getId());
        if (checkIntersectionWithAllTasks(subTask)) return null;
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTasks.get(subTask.getId()).getEpicId());
        updateEpicStatus(epic);
        updateEpicsDurationStartEndDate(epic);
        if (subTask.getStartTime() != null) {
            allTasksSortList.add(subTask);
        }
        return subTask;
    }

    @Override
    public void deleteTask(int taskId) {
        allTasksSortList.remove(tasks.get(taskId));
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            historyManager.remove(taskId);
        }
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            epics.get(epicId).getSubTasksIds()
                    .forEach(subTaskId -> {
                        allTasksSortList.remove(subTasks.get(subTaskId));
                        subTasks.remove(subTaskId);
                        historyManager.remove(subTaskId);
                    });

            allTasksSortList.remove(epics.get(epicId));
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubTask(int subTasksId) {
        allTasksSortList.remove(subTasks.get(subTasksId));
        if (subTasks.containsKey(subTasksId)) {
            Epic epic = epics.get(subTasks.get(subTasksId).getEpicId());
            epic.removeSubTaskId(subTasksId);
            subTasks.remove(subTasksId);
            historyManager.remove(subTasksId);
            updateEpicStatus(epic);
            updateEpicsDurationStartEndDate(epic);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        historyManager.removeAllTasks();
        allTasksSortList.clear();
    }

    @Override
    public Task addNewTask(Task task) {
        allTasksSortList.remove(task);
        if (checkIntersectionWithAllTasks(task)) return null;
        task.setId(++identifier);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            allTasksSortList.add(task);
        }
        return task;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        if (checkIntersectionWithAllTasks(epic)) return null;
        epic.setId(++identifier);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addNewSubTask(SubTask subTask) {
        allTasksSortList.remove(subTask);
        if (checkIntersectionWithAllTasks(subTask)) return null;
        subTask.setId(++identifier);
        epics.get(subTask.getEpicId()).addSubTaskId(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTasks.get(subTask.getId()).getEpicId());
        updateEpicStatus(epic);
        updateEpicsDurationStartEndDate(epic);
        if (subTask.getStartTime() != null) {
            allTasksSortList.add(subTask);
        }
        return subTask;
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
            if (!subTasks.get(subTaskIdInEpic).getTaskStatus().equals(NEW)) {
                flagNew = false;
                break;
            }
        }

        for (Integer subTaskIdInEpic : subTaskIdsInEpic) {
            if (!subTasks.get(subTaskIdInEpic).getTaskStatus().equals(DONE)) {
                flagDone = false;
                break;
            }
        }

        if (flagNew) {
            epic.setTaskStatus(NEW);
        } else if (flagDone) {
            epic.setTaskStatus(DONE);
        } else {
            epic.setTaskStatus(IN_PROGRESS);
        }
    }

    private void updateEpicsDurationStartEndDate(Epic epic) {
        List<Integer> subTaskIds = epic.getSubTasksIds();
        LocalDateTime epicStartTime = null;
        LocalDateTime epicEndTime = null;
        Duration duration = Duration.ZERO;

        for (int subTaskId : subTaskIds) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask == null || subTask.getStartTime() == null || subTask.getEndTime() == null) continue;

            if (epicStartTime == null || subTask.getStartTime().isBefore(epicStartTime)) {
                epicStartTime = subTask.getStartTime();
            }
            if (epicEndTime == null || subTask.getEndTime().isAfter(epicEndTime)) {
                epicEndTime = subTask.getEndTime();
            }

            duration = duration.plus(subTask.getDuration());
        }

        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(duration);
    }

    private boolean checkIntersectionTwoTasks(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task2.getStartTime().isBefore(task1.getEndTime());
    }

    private boolean checkIntersectionWithAllTasks(Task checkTask) {
        boolean flag = false;
        for (Task task : getPrioritisedTasks()) {
            if (checkIntersectionTwoTasks(task, checkTask))
                throw new CheckIntersectionWithAllTasksException("Задача с датой начала " +
                        checkTask.getStartTime() + " уже существует");
        }
        return flag;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
