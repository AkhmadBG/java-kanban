package managers.impl;

import exceptions.ManagerSaveException;
import managers.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static enums.TaskStatus.DONE;
import static enums.TaskStatus.IN_PROGRESS;
import static java.time.Month.*;
import static utils.CSVFormat.fromString;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        String firstString = "id,type,name,description,status,duration,startTime,endTime,epic";
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            if (file.length() == 0) {
                bufferedWriter.write(firstString);
                bufferedWriter.write("\n");
            }
            getAllTasks().stream()
                    .sorted(Comparator.comparing(Task::getId))
                    .map(Task::toString)
                    .forEach(taskStr -> {
                        try {
                            bufferedWriter.write(taskStr);
                            bufferedWriter.write("\n");
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить задачи: " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            fileBackedTaskManager = new FileBackedTaskManager(file);
            while (bufferedReader.ready()) {
                String taskString = bufferedReader.readLine();
                if (!taskString.equals("id,type,name,description,status,duration,startTime,endTime,epic")) {
                    String[] arrayTaskString = taskString.split(",");
                    switch (arrayTaskString[1]) {
                        case "TASK_TYPE":
                            Task task = fromString(taskString);
                            fileBackedTaskManager.getTasks().put(Integer.parseInt(arrayTaskString[0]), task);
                            fileBackedTaskManager.getPrioritisedTasks().add(task);
                            break;
                        case "EPIC_TYPE":
                            Epic epic = (Epic) fromString(taskString);
                            fileBackedTaskManager.getEpics().put(Integer.parseInt(arrayTaskString[0]), epic);
                            break;
                        case "SUBTASK_TYPE":
                            SubTask subTask = (SubTask) fromString(taskString);
                            fileBackedTaskManager.getSubTasks().put(Integer.parseInt(arrayTaskString[0]), subTask);
                            fileBackedTaskManager.getPrioritisedTasks().add(subTask);
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

        // Создание таскменеджера 1
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(file1);

        // Создание первой задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setDuration(Duration.parse("PT30M"));
        task1.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
        fileBackedTaskManager1.addNewTask(task1);

        // Создание первого эпика
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        fileBackedTaskManager1.addNewEpic(epic1);

        // Создание первой подзадачи
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", 2);
        subTask1.setDuration(Duration.parse("PT30M"));
        subTask1.setStartTime(LocalDateTime.of(1999, DECEMBER, 31, 5, 5));
        fileBackedTaskManager1.addNewSubTask(subTask1);

        // Создание второй подзадачи
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", 2);
        subTask2.setDuration(Duration.parse("PT30M"));
        subTask2.setStartTime(LocalDateTime.of(2003, MARCH, 3, 3, 3));
        fileBackedTaskManager1.addNewSubTask(subTask2);

        // Создание третьей подзадачи
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", 2);
        subTask3.setDuration(Duration.parse("PT30M"));
        subTask3.setStartTime(LocalDateTime.of(2010, JANUARY, 1, 0, 0));
        fileBackedTaskManager1.addNewSubTask(subTask3);

        // Получение задач для формирования истории fileBackedTaskManager1
        fileBackedTaskManager1.getTask(1);
        fileBackedTaskManager1.getEpic(2);
        fileBackedTaskManager1.getSubTask(3);
        fileBackedTaskManager1.getSubTask(4);
        fileBackedTaskManager1.getSubTask(5);

        // Печать таскменеджера 1
        printAllTasks(fileBackedTaskManager1);

        // Печать задач таскменеджера 1 отсортированные по времени
        fileBackedTaskManager1.getPrioritisedTasks()
                .forEach(System.out::println);

        // Обновление первой задачи
        Task updatedTask = new Task("Обновление задачи 1", "Обновление описания задачи 1");
        updatedTask.setTaskStatus(IN_PROGRESS);
        updatedTask.setDuration(Duration.parse("PT90M"));
        updatedTask.setStartTime(LocalDateTime.of(2025, DECEMBER, 10, 20, 5));
        updatedTask.setId(1);
        fileBackedTaskManager1.updateTask(updatedTask);

        // Обновление первого эпика
        Epic updatedEpic = new Epic("Обновление эпика 1", "Обновление описания эпика 1");
        updatedEpic.setId(2);
        fileBackedTaskManager1.updateEpic(updatedEpic);

        // Обновление первой подзадачи
        SubTask updatedSubTask1 = new SubTask("Обновление подзадачи 1", "Обновление описания подзадачи 1", 2);
        updatedSubTask1.setTaskStatus(DONE);
        updatedSubTask1.setDuration(Duration.parse("PT100M"));
        updatedSubTask1.setStartTime(LocalDateTime.of(2027, MAY, 11, 11, 11));
        updatedSubTask1.setId(3);
        fileBackedTaskManager1.updateSubTask(updatedSubTask1);

        // Обновление второй подзадачи
        SubTask updatedSubTask2 = new SubTask("Обновление подзадачи 2", "Обновление описания подзадачи 2", 2);
        updatedSubTask2.setTaskStatus(DONE);
        updatedSubTask2.setDuration(Duration.parse("PT100M"));
        updatedSubTask2.setStartTime(LocalDateTime.of(2026, JULY, 11, 11, 11));
        updatedSubTask2.setId(4);
        fileBackedTaskManager1.updateSubTask(updatedSubTask2);

        // Обновление третьей подзадачи
        SubTask updatedSubTask3 = new SubTask("Обновление подзадачи 3", "Обновление описания подзадачи 3", 2);
        updatedSubTask3.setTaskStatus(DONE);
        updatedSubTask3.setDuration(Duration.parse("PT100M"));
        updatedSubTask3.setStartTime(LocalDateTime.of(2028, JUNE, 11, 11, 11));
        updatedSubTask3.setId(5);
        fileBackedTaskManager1.updateSubTask(updatedSubTask3);

        System.out.println("-".repeat(50));

        // Создание таскменеджера 2 из файла
        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file1);

        // Получение задач для формирования истории fileBackedTaskManager2
        fileBackedTaskManager2.getTask(1);
        fileBackedTaskManager2.getEpic(2);
        fileBackedTaskManager2.getSubTask(3);
        fileBackedTaskManager2.getSubTask(4);
        fileBackedTaskManager2.getSubTask(5);

        // Печать таскменеджера 2
        printAllTasks(fileBackedTaskManager2);

        // Печать задач таскменеджера 2 отсортированные по времени
        fileBackedTaskManager2.getPrioritisedTasks()
                .forEach(System.out::println);
    }


    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        taskManager.getTasks().values()
                .forEach(System.out::println);

        System.out.println("Эпики:");
        taskManager.getEpics().values().stream()
                .peek(System.out::println)
                .flatMap(epic -> taskManager.getAllSubTasksInEpic(epic.getId()).stream())
                .map(subTask -> "-->" + subTask)
                .forEach(System.out::println);

        System.out.println("Подзадачи:");
        taskManager.getSubTasks().values()
                .forEach(System.out::println);

        System.out.println("История:");
        taskManager.getHistory()
                .forEach(System.out::println);
    }

}
