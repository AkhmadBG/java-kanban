import managers.TaskManager;

public class Main {

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

    public static void main(String[] args) {

    }

}