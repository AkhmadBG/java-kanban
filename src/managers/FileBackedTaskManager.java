package managers;

import enums.TaskStatus;
import enums.TaskType;
import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.*;
import java.util.*;

import static enums.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void save() {
        String firstString = "id,type,name,description,status,epic";
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            if (file.length() == 0) {
                bufferedWriter.write(firstString);
                bufferedWriter.write("\n");
            }
            List<Task> sortListTasks = getAllTasks().stream()
                    .sorted(Comparator.comparing(Task::getId))
                    .toList();
            for (Task task : sortListTasks) {
                bufferedWriter.write(task.toString());
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить задачи: " + e.getMessage());
        }
    }

    public Task fromString(String value) {
        String[] arrayTaskString = value.split(",");

        int id = Integer.parseInt(arrayTaskString[0]);
        TaskType taskType = TaskType.valueOf(arrayTaskString[1]);
        String name = arrayTaskString[2];
        String description = arrayTaskString[3];
        TaskStatus taskStatus = TaskStatus.valueOf(arrayTaskString[4]);

        switch (arrayTaskString[1]) {
            case "TASK_TYPE":
                Task task = new Task(taskType, name, description, taskStatus);
                task.setId(id);
                return task;
            case "EPIC_TYPE":
                Epic epic = new Epic(taskType, name, description, taskStatus);
                epic.setId(id);
                if (arrayTaskString.length > 5) {
                    String[] arraySubTasksIds = arrayTaskString[5].split(",");
                    ArrayList<Integer> subTasksIdsFromString = new ArrayList<>();
                    for (String subTaskId : arraySubTasksIds) {
                        subTasksIdsFromString.add(Integer.parseInt(subTaskId));
                    }
                    epic.setSubTasksIds(subTasksIdsFromString);
                }
                epic.setId(id);
                return epic;
            case "SUBTASK_TYPE":
                SubTask subTask = new SubTask(taskType, name, description, taskStatus,
                        Integer.parseInt(arrayTaskString[5]));
                subTask.setId(id);
                return subTask;
            default:
                return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            fileBackedTaskManager = new FileBackedTaskManager(file);
            while (bufferedReader.ready()) {
                String taskString = bufferedReader.readLine();
                if (!taskString.equals("id,type,name,description,status,epic")) {
                    String[] arrayTaskString = taskString.split(",");
                    switch (arrayTaskString[1]) {
                        case "TASK_TYPE":
                            fileBackedTaskManager.tasks.put(Integer.parseInt(arrayTaskString[0]),
                                    fileBackedTaskManager.fromString(taskString));
                            break;
                        case "EPIC_TYPE":
                            fileBackedTaskManager.epics.put(Integer.parseInt(arrayTaskString[0]),
                                    (Epic) fileBackedTaskManager.fromString(taskString));
                            break;
                        case "SUBTASK_TYPE":
                            fileBackedTaskManager.subTasks.put(Integer.parseInt(arrayTaskString[0]),
                                    (SubTask) fileBackedTaskManager.fromString(taskString));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTaskManager;
    }

    @Override
    public Task createTask(TaskType type, String name, String description) {
        Task task = super.createTask(type, name, description);
        save();
        return task;
    }

    @Override
    public Epic createEpic(TaskType type, String name, String description) {
        Epic epic = super.createEpic(type, name, description);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(TaskType type, String name, String description, int epicId) {
        SubTask subTask = super.createSubTask(type, name, description, epicId);
        save();
        return subTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updateTask = super.updateTask(task);
        save();
        return updateTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updateEpic = super.updateEpic(epic);
        save();
        return updateEpic;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        SubTask updateSubTask = super.updateSubTask(subTask);
        save();
        return updateSubTask;
    }

    @Override
    public Task addNewTask(Task task) {
        Task newTask = super.addNewTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        Epic newEpic = super.addNewEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public SubTask addNewSubTask(SubTask subTask) {
        SubTask newSubTask = super.addNewSubTask(subTask);
        save();
        return newSubTask;
    }

    public static void main(String[] args) {

        File file1 = new File("fileBackedTaskManager.csv");

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file1);

        fileBackedTaskManager.createTask(TASK_TYPE, "Задача 1", "Описание задачи");
        fileBackedTaskManager.createEpic(EPIC_TYPE, "Эпик 1", "Описание эпика");
        fileBackedTaskManager.createSubTask(SUBTASK_TYPE, "Подзадача 1", "Описание подзадачи", 2);
        fileBackedTaskManager.createSubTask(SUBTASK_TYPE, "Подзадача 1", "Описание подзадачи", 2);

        System.out.println(fileBackedTaskManager.getEpic(2));

        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file1);

        System.out.println(fileBackedTaskManager1.getTask(1));
        System.out.println(fileBackedTaskManager1.getEpic(2));
        System.out.println(fileBackedTaskManager1.getSubTask(3));

    }

}
