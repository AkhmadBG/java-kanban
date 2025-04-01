import managers.Managers;
import managers.TaskManager;
import model.Task;

public class Main {

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getAllSubTasksInEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

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

        taskManager.createTask("Сходить в кино", "Купить билеты в кино");

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubTask(4);
        taskManager.getSubTask(5);
        taskManager.getSubTask(6);
        taskManager.getEpic(7);
        taskManager.getSubTask(8);
        taskManager.getSubTask(9);
        taskManager.getSubTask(10);
        taskManager.getTask(11);

        printAllTasks(taskManager);

    }
}