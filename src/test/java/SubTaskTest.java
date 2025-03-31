import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    private SubTask subTask;

    @BeforeEach
    void setUp() {
        subTask = new SubTask("Тестовая подзадача", "Описание тестовой подзадачи", 1);
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
        String expected = "SubTask { id=" + subTask.getId() + ", name='Тестовая подзадача', description='Описание тестовой подзадачи', taskStatus=" + subTask.getTaskStatus() + ", epicId=1 }";
        assertEquals(expected, subTask.toString(), "Вывод должен соответствовать формату");
    }
}