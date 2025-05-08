package managers;

import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import utils.CSVFormat;

import java.io.*;
import java.util.*;

import static enums.TaskStatus.DONE;
import static enums.TaskStatus.IN_PROGRESS;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    private void save() {
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

    @Override
    public Task createTask(String name, String description) {
        Task task = super.createTask(name, description);
        save();
        return task;
    }

    @Override
    public Epic createEpic(String name, String description) {
        Epic epic = super.createEpic(name, description);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(String name, String description, int epicId) {
        SubTask subTask = super.createSubTask(name, description, epicId);
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

        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(file1);

        fileBackedTaskManager1.createTask("Задача 1", "Описание задачи 1");
        fileBackedTaskManager1.createTask("Задача 2", "Описание задачи 2");

        fileBackedTaskManager1.createEpic("Эпик 1", "Описание эпика 1");
        fileBackedTaskManager1.createSubTask("Подзадача 1", "Описание подзадачи 1", 3);
        fileBackedTaskManager1.createSubTask("Подзадача 2", "Описание подзадачи 2", 3);
        fileBackedTaskManager1.createSubTask("Подзадача 3", "Описание подзадачи 3", 3);

        fileBackedTaskManager1.createEpic("Эпик 2", "Описание эпика 2");
        fileBackedTaskManager1.createSubTask("Подзадача 4", "Описание подзадачи 4", 7);
        fileBackedTaskManager1.createSubTask("Подзадача 5", "Описание подзадачи 5", 7);
        fileBackedTaskManager1.createSubTask("Подзадача 6", "Описание подзадачи 6", 7);

        printAllTasks(fileBackedTaskManager1);

        Task task1 = new Task("Обновление задачи 1", "Обновление описания задачи 1", IN_PROGRESS);
        task1.setId(1);
        fileBackedTaskManager1.updateTask(task1);

        Epic epic3 = new Epic("Обновление эпика 1", "Обновление описания эпика 1", DONE);
        epic3.setId(3);
        fileBackedTaskManager1.updateEpic(epic3);

        SubTask subTask4 = new SubTask("Обновление подзадачи 4", "Обновление описания подзадачи 4", IN_PROGRESS, 7);
        subTask4.setId(8);
        fileBackedTaskManager1.updateSubTask(subTask4);

        SubTask subTask5 = new SubTask("Обновление подзадачи 5", "Обновление описания подзадачи 5", IN_PROGRESS, 7);
        subTask5.setId(9);
        fileBackedTaskManager1.updateSubTask(subTask5);

        SubTask subTask6 = new SubTask("Обновление подзадачи 6", "Обновление описания подзадачи 6", IN_PROGRESS, 7);
        subTask6.setId(10);
        fileBackedTaskManager1.updateSubTask(subTask6);

        System.out.println("-".repeat(50));

        FileBackedTaskManager fileBackedTaskManager2 = CSVFormat.loadFromFile(file1);

        printAllTasks(fileBackedTaskManager2);

    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task task : taskManager.getTasks().values()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpics().values()) {
            System.out.println(epic);

            for (Task task : taskManager.getAllSubTasksInEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubTasks().values()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }

}
