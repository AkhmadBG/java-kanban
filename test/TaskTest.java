import enums.TaskStatus;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static enums.TaskType.TASK_TYPE;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(TASK_TYPE, "Задача 1", "Описание задачи");
    }

    @Test
    void testConstructor() {
        assertEquals("Задача 1", task.getName());
        assertEquals("Описание задачи", task.getDescription());
        assertEquals(TaskStatus.NEW, task.getTaskStatus());
    }

    @Test
    void testSetAndGetId() {
        task.setId(1);
        assertEquals(1, task.getId());
    }

    @Test
    void testSetAndGetName() {
        task.setName("Новое название тестовой задачи");
        assertEquals("Новое название тестовой задачи", task.getName());
    }

    @Test
    void testSetAndGetDescription() {
        task.setDescription("Новое описание тестовой задачи");
        assertEquals("Новое описание тестовой задачи", task.getDescription());
    }

    @Test
    void testSetAndGetTaskStatus() {
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getTaskStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Task task1 = new Task(TASK_TYPE, "Задача 1", "Описание задачи");
        task1.setId(100);

        Task task2 = new Task(TASK_TYPE, "Задача 1", "Описание задачи");
        task2.setId(100);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testToString() {
        task.setId(1);
        String expected = "1,TASK_TYPE,Задача 1,Описание задачи,NEW";
        assertEquals(expected, task.toString());
    }
}
