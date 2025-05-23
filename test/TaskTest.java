import enums.TaskStatus;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static enums.TaskStatus.IN_PROGRESS;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Задача 1", "Описание задачи 1");
        task.setDuration(Duration.parse("PT30M"));
        task.setStartTime(LocalDateTime.of(2001, JANUARY, 1, 1, 1));
    }

    @Test
    void testConstructor() {
        assertEquals("Задача 1", task.getName());
        assertEquals("Описание задачи 1", task.getDescription());
        assertEquals(Duration.parse("PT30M"), task.getDuration());
        assertEquals(LocalDateTime.of(2001, JANUARY, 1, 1, 1), task.getStartTime());
        assertEquals(LocalDateTime.of(2001, JANUARY, 1, 1, 31), task.getEndTime());
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
        task.setTaskStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, task.getTaskStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Task task1 = new Task("Задача 1", "Описание задачи");
        task1.setId(100);

        Task task2 = new Task("Задача 1", "Описание задачи");
        task2.setId(100);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testToString() {
        task.setId(1);
        String expected = "1,TASK_TYPE,Задача 1,Описание задачи 1,NEW,30,01:01 01.01.2001,01:31 01.01.2001";
        assertEquals(expected, task.toString());
    }

}
