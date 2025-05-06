import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static enums.TaskType.SUBTASK_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {

    private SubTask subTask;

    @BeforeEach
    void setUp() {
        subTask = new SubTask(SUBTASK_TYPE, "Подзадача 1", "Описание подзадачи", 1);
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
        String expected = "0,SUBTASK_TYPE,Подзадача 1,Описание подзадачи,NEW,1";
        assertEquals(expected, subTask.toString(), "Вывод должен соответствовать формату");
    }
}