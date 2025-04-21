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

//        taskManager.createTask("Задача 1", "Описание задачи 1");
//        taskManager.createTask("Задача 2", "Описание задачи 2");
//
//        taskManager.createEpic("Эпик 1", "Описание эпика 1");
//        taskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", 3);
//        taskManager.createSubTask("Подзадача 2", "Описание подзадачи 2", 3);
//        taskManager.createSubTask("Подзадача 3", "Описание подзадачи 3", 3);
//
//        taskManager.createEpic("Эпик 2", "Описание эпика 2");
//        taskManager.createSubTask("Подзадача 4", "Описание подзадачи 4", 7);
//        taskManager.createSubTask("Подзадача 5", "Описание подзадачи 5", 7);
//        taskManager.createSubTask("Подзадача 6", "Описание подзадачи 6", 7);
//
//        taskManager.createTask("Задача 3", "Описание задачи 3");
//
//        taskManager.getTask(1);
//        taskManager.getTask(2);
//        taskManager.getEpic(3);
//        taskManager.getSubTask(4);
//        taskManager.getSubTask(5);
//        taskManager.getSubTask(6);
//        taskManager.getEpic(7);
//        taskManager.getSubTask(8);
//        taskManager.getSubTask(9);
//        taskManager.getSubTask(10);
//        taskManager.getTask(11);
//        taskManager.getTask(11);
//        taskManager.getSubTask(10);
//        taskManager.getTask(11);
//        taskManager.getTask(11);
//        taskManager.getTask(11);
//
//        taskManager.deleteTask(1);
//        taskManager.deleteEpic(3);
//        taskManager.deleteSubTask(8);
//
//        taskManager.getTask(2);
//        taskManager.getTask(2);

        printAllTasks(taskManager);

    }
}