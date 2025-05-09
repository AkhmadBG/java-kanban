import managers.InMemoryTaskManager;
import managers.TaskManager;
import model.Task;

public class Main {

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

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        inMemoryTaskManager.createTask("Задача 1", "Описание задачи 1");
        inMemoryTaskManager.createTask("Задача 2", "Описание задачи 2");

        inMemoryTaskManager.createEpic("Эпик 1", "Описание эпика 1");
        inMemoryTaskManager.createSubTask("Подзадача 1", "Описание подзадачи 1", 3);
        inMemoryTaskManager.createSubTask("Подзадача 2", "Описание подзадачи 2", 3);
        inMemoryTaskManager.createSubTask("Подзадача 3", "Описание подзадачи 3", 3);

        inMemoryTaskManager.createEpic("Эпик 2", "Описание эпика 2");
        inMemoryTaskManager.createSubTask("Подзадача 4", "Описание подзадачи 4", 7);
        inMemoryTaskManager.createSubTask("Подзадача 5", "Описание подзадачи 5", 7);
        inMemoryTaskManager.createSubTask("Подзадача 6", "Описание подзадачи 6", 7);

        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubTask(4);
        inMemoryTaskManager.getSubTask(5);
        inMemoryTaskManager.getSubTask(6);
        inMemoryTaskManager.getEpic(7);
        inMemoryTaskManager.getSubTask(8);
        inMemoryTaskManager.getSubTask(9);
        inMemoryTaskManager.getSubTask(10);

        printAllTasks(inMemoryTaskManager);

    }
}