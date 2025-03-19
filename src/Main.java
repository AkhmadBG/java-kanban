public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

//        System.out.println("проверка создания задач и получения всех задач");
//
//        taskManager.createTask("Сделать домашку", "Сделать всю домашку до конца недели");
//        taskManager.createTask("Сходить на прогулку", "Сходить в парк на прогулку");
//
//        taskManager.createEpic("Сделать уборку дома", "Обязательно сделать генеральную уборку дома");
//        taskManager.createSubTask("Пропылесосить полы", "Найти пылесос", 3);
//        taskManager.createSubTask("Помыть полы", "Хорошо помыть полы", 3);
//        taskManager.createSubTask("Помыть посуду", "Помыть всю посуду", 3);
//
//        taskManager.createEpic("Съездить в отпуск", "В отпуске надо хорошо отдохнуть");
//        taskManager.createSubTask("Собрать чемодан", "Сложить вещи", 7);
//        taskManager.createSubTask("Купить билеты", "Билеты на поезд", 7);
//        taskManager.createSubTask("Забронировать отель", "Отель 5 звезд", 7);
//
//        System.out.println(taskManager.getAllTasks());
//
//        System.out.println("Проверка получения задач по Id");
//
//        Task task = taskManager.getTask(1);
//        System.out.println(task);
//        Epic epic = (Epic) taskManager.getEpic(3);
//        System.out.println(epic);
//        SubTask subTask = (SubTask) taskManager.getSubTask(4);
//        System.out.println(subTask);
//
//        System.out.println("Проверка обновления задач");
//
//        Task taskCheckUpdate = new Task("Сходить в магазин", "Купить все по списку");
//        taskManager.addNewTask(taskCheckUpdate);
//        System.out.println(taskManager.getTask(taskCheckUpdate.getId()));
//        taskCheckUpdate.setName("Сходил в магазин");
//        taskCheckUpdate.setDescription("Купить все по списку");
//        taskCheckUpdate.setTaskStatus(TaskStatus.DONE);
//        taskManager.updateTask(taskCheckUpdate);
//        System.out.println(taskManager.getTask(taskCheckUpdate.getId()));
//
//        Epic epicCheckUpdate = new Epic("Сделать ремонт", "Хороший ремонт");
//        taskManager.addNewEpic(epicCheckUpdate);
//        System.out.println(taskManager.getEpic(epicCheckUpdate.getId()));
//        epicCheckUpdate.setName("Делаю ремонт");
//        epicCheckUpdate.setDescription("Очень хороший ремонт");
//        taskManager.updateEpic(epicCheckUpdate);
//        System.out.println(taskManager.getEpic(epicCheckUpdate.getId()));
//
//        SubTask subTaskCheckUpdate = new SubTask("Сделать полы", "Полы из ламината", epicCheckUpdate.getId());
//        taskManager.addNewSubTask(subTaskCheckUpdate);
//        System.out.println(taskManager.getSubTask(subTaskCheckUpdate.getId()));
//        subTaskCheckUpdate.setName("Сделал полы");
//        subTaskCheckUpdate.setDescription("Полы из досок");
//        subTaskCheckUpdate.setTaskStatus(TaskStatus.DONE);
//        taskManager.updateSubTask(subTaskCheckUpdate);
//        System.out.println(taskManager.getSubTask(subTaskCheckUpdate.getId()));
//
//        System.out.println("Проверка удаления задач по Id");
//
//        System.out.println(taskManager.getAllTasks());
//        taskManager.deleteTask(1);
//        taskManager.deleteEpic(3);
//        taskManager.deleteSubTask(4);
//        taskManager.deleteSubTask(5);
//        taskManager.deleteSubTask(6);
//        System.out.println(taskManager.getAllTasks());
//
//        System.out.println("Проверка удаления всех задач");
//
//        System.out.println(taskManager.getAllTasks());
//        taskManager.deleteAllTasks();
//        System.out.println(taskManager.getAllTasks());

        //System.out.println("Проверка изменения статуса Epic");

        Epic epic1 = new Epic("Сделать уборку дома", "Обязательно сделать генеральную уборку дома");
        taskManager.addNewEpic(epic1);
        System.out.println(epic1);

        SubTask subTask1 = new SubTask("Пропылесосить полы", "Найти пылесос", epic1.getId());
        taskManager.addNewSubTask(subTask1);

        SubTask subTask2 = new SubTask("Помыть полы", "Хорошо помыть полы", epic1.getId());
        taskManager.addNewSubTask(subTask2);

        SubTask subTask3 = new SubTask("Помыть полы", "Хорошо помыть полы", epic1.getId());
        taskManager.addNewSubTask(subTask3);

        subTask1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        System.out.println(subTask1);
        System.out.println(epic1);

        subTask2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask2);
        System.out.println(subTask2);
        System.out.println(epic1);

        subTask3.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask3);
        System.out.println(subTask3);
        System.out.println(epic1);

        taskManager.deleteSubTask(subTask3.getId());
        System.out.println(epic1);

    }
}