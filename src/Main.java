import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в трекер задач!");
        TaskManager taskManager = new TaskManager();
        initialTaskManager(taskManager);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMainMenu();
            String mainCommand = scanner.nextLine();
            switch (mainCommand) {
                case "1":
                    if (!taskManager.getTasks().isEmpty() ||
                            !taskManager.getEpics().isEmpty() ||
                            !taskManager.getSubTasks().isEmpty()) {
                        for (Map.Entry<Integer, Task> task : taskManager.getTasks().entrySet()) {
                            System.out.println(task);
                        }
                        for (Map.Entry<Integer, Epic> epic : taskManager.getEpics().entrySet()) {
                            System.out.println(epic);
                        }
                        for (Map.Entry<Integer, SubTask> subTask : taskManager.getSubTasks().entrySet()) {
                            System.out.println(subTask);
                        }
                    } else {
                        System.out.println("В таскменеджере нет задач");
                    }
                    break;
                case "2":
                    printSubMenu();
                    String createCommand = scanner.nextLine();
                    switch (createCommand) {
                        case "1":
                            System.out.print("Введите название задачи: ");
                            String taskName = scanner.nextLine();
                            System.out.print("Введите описание задачи: ");
                            String taskDescription = scanner.nextLine();
                            taskManager.createTask(taskName, taskDescription);
                            System.out.println("Задача добавлена");
                            break;
                        case "2":
                            System.out.print("Введите название эпика: ");
                            String epicName = scanner.nextLine();
                            System.out.print("Введите описание эпика: ");
                            String epicDescription = scanner.nextLine();
                            taskManager.createEpic(epicName, epicDescription);
                            System.out.println("Эпик добавлен");
                            break;
                        case "3":
                            System.out.print("Введите название подзадачи: ");
                            String subTaskName = scanner.nextLine();
                            System.out.print("Введите описание подзадачи: ");
                            String sabTaskDescription = scanner.nextLine();
                            System.out.print("Введите номер эпика, в который надо добавить подзачу: ");
                            int epicId = scanner.nextInt();
                            scanner.nextLine();
                            taskManager.createSubTask(subTaskName, sabTaskDescription, epicId);
                            System.out.println("Подзадача добавлена");
                            break;
                        default:
                            System.out.println("Такого типа задачи нет");
                    }
                    break;
                case "3":
                    printSubMenu();
                    String readCommand = scanner.nextLine();
                    switch (readCommand) {
                        case "1":
                            System.out.print("Введите номер задачи: ");
                            int taskId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println(taskManager.readTask(taskId));
                            break;
                        case "2":
                            System.out.print("Введите номер эпика: ");
                            int epicId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println(taskManager.readEpic(epicId));
                            break;
                        case "3":
                            System.out.print("Введите номер подзадачи: ");
                            int subTaskId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println(taskManager.readSubTask(subTaskId));
                            break;
                        default:
                            System.out.println("Такого типа задачи нет");
                    }
                    break;
                case "4":
                    printSubMenu();
                    String updateCommand = scanner.nextLine();
                    switch (updateCommand) {
                        case "1":
                            System.out.print("Введите номер задачи: ");
                            int taskId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Введите название задачи: ");
                            String taskName = scanner.nextLine();
                            System.out.print("Введите описание задачи: ");
                            String taskDescription = scanner.nextLine();
                            System.out.print("Введите статус задачи: ");
                            TaskStatus taskStatus = TaskStatus.valueOf(scanner.nextLine());
                            taskManager.updateTask(taskId, taskName, taskDescription, taskStatus);
                            System.out.println("Задача обновлена");
                            break;
                        case "2":
                            System.out.print("Введите номер эпика: ");
                            int epicId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Введите название эпика: ");
                            String epicName = scanner.nextLine();
                            System.out.print("Введите описание эпика: ");
                            String epicDescription = scanner.nextLine();
                            System.out.print("Введите статус эпика: ");
                            taskManager.updateEpic(epicId, epicName, epicDescription);
                            System.out.println("Эпик обновлен");
                            break;
                        case "3":
                            System.out.print("Введите номер подзадачи: ");
                            int subTaskId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Введите название подзадачи: ");
                            String subTaskName = scanner.nextLine();
                            System.out.print("Введите описание подзадачи: ");
                            String subTaskDescription = scanner.nextLine();
                            System.out.print("Введите статус подзадачи: ");
                            TaskStatus subTaskStatus = TaskStatus.valueOf(scanner.nextLine());
                            taskManager.updateSubTask(subTaskId, subTaskName, subTaskDescription, subTaskStatus);
                            System.out.println("Подзадача обновлена");
                            break;
                        default:
                            System.out.println("Такого типа задачи нет");
                    }
                    break;
                case "5":
                    printSubMenu();
                    String deleteCommand = scanner.nextLine();
                    switch (deleteCommand) {
                        case "1":
                            System.out.print("Введите номер задачи: ");
                            int taskId = scanner.nextInt();
                            scanner.nextLine();
                            taskManager.deleteTask(taskId);
                            System.out.println("Задача удалена.");
                            break;
                        case "2":
                            System.out.print("Введите номер эпика: ");
                            int epicId = scanner.nextInt();
                            scanner.nextLine();
                            taskManager.deleteEpic(epicId);
                            System.out.println("Эпик удален");
                            break;
                        case "3":
                            System.out.print("Введите номер подзадачи: ");
                            int subTaskId = scanner.nextInt();
                            scanner.nextLine();
                            taskManager.deleteSubTask(subTaskId);
                            System.out.println("Задача удалена");
                            break;
                        default:
                            System.out.println("Такого типа задачи нет");
                    }
                    break;
                case "6":
                    taskManager.deleteAllTasks();
                    taskManager.setIdentifier(0);
                    System.out.println("Все задачи удалены!");
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Такой команды нет");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("Выберите команду: ");
        System.out.println("1 - Просмотр всех задач");
        System.out.println("2 - Добавить задачу");
        System.out.println("3 - Просмотр задачи");
        System.out.println("4 - Изменить задачу");
        System.out.println("5 - Удалить задачу");
        System.out.println("6 - Удалить все задачи");
        System.out.println("7 - Выход");
        System.out.println("-".repeat(20));
    }

    private static void printSubMenu() {
        System.out.println();
        System.out.println("Выберите тип задачи: ");
        System.out.println("1 - Task");
        System.out.println("2 - Epic");
        System.out.println("3 - SubTask");
        System.out.println("-".repeat(20));
    }

    public static void initialTaskManager(TaskManager taskManager) {
        taskManager.createTask("Сделать домашку", "Сделать всю домашку до конца недели");
        taskManager.createTask("Сходить на прогулку", "Сходить в парк на прогулку");

        taskManager.createEpic("Сделать уборку дома", "Обязательно сделать генеральную уборку дома");
        taskManager.createSubTask("Пропылесосить полы", "Найти пылесос", 3);
        taskManager.createSubTask("Помыть полы", "Хорошо помыть полы", 3);
        taskManager.createSubTask("Помыть посуду", "Помыть всю посуду", 3);

        taskManager.createEpic("Съездить в отпуск", "В отпуске надо хорошо отдохнуть");
        taskManager.createSubTask("Собрать чемодан", "Сложить вещи", 7);
        taskManager.createSubTask("Купить билеты", "Билеты на поезд", 7);
        taskManager.createSubTask("Забронировать отель", "Отель 5 звезд", 7);

    }
}