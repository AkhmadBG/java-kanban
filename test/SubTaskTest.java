import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {

    private SubTask subTask;

    @BeforeEach
    void setUp() {
        subTask = new SubTask("Подзадача 1",
                "Описание подзадачи 1",
                Duration.parse("PT30M"),
                LocalDateTime.of(1999, DECEMBER, 31, 5, 5), 1);
    }

    @Test
    void testGetEpicId() {
        assertEquals(1, subTask.getEpicId(), "Должно быть 1");
    }

    @Test
    void testSetEpicId() {
        subTask.setEpicId(2);
        assertEquals(2, subTask.getEpicId(), "Должно быть 2");
    }

    @Test
    void testToString() {
        String expected = "0,SUBTASK_TYPE,Подзадача 1,Описание подзадачи 1,NEW,30,05:05 31.12.1999,05:35 31.12.1999,1";
        assertEquals(expected, subTask.toString(), "Вывод должен соответствовать формату");
    }

}