import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class EpicTest {
    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("Тестовый эпик", "Описание тестового эпика");
    }

    @Test
    void testAddSubTaskId() {
        epic.addSubTaskId(1);
        epic.addSubTaskId(2);
        List<Integer> expected = List.of(1, 2);
        assertEquals(expected, epic.getSubTasksIds(), "Должно быть 1, 2");
    }

    @Test
    void testRemoveSubTaskId() {
        epic.addSubTaskId(1);
        epic.addSubTaskId(2);
        epic.removeSubTaskId(1);
        List<Integer> expected = List.of(2);
        assertEquals(expected, epic.getSubTasksIds(), "Должно быть 2");
    }

    @Test
    void testSetSubTasksIds() {
        ArrayList<Integer> newIds = new ArrayList<>(List.of(3, 4, 5));
        epic.setSubTasksIds(newIds);
        assertEquals(newIds, epic.getSubTasksIds(), "Должно быть 3, 4, 5");
    }

    @Test
    void testToString() {
        epic.addSubTaskId(10);
        epic.addSubTaskId(20);
        String expected = "Epic { id=" + epic.getId() + ", name='Тестовый эпик', description='Описание тестового эпика', taskStatus=" + epic.getTaskStatus() + ", subTasksIds=[10, 20] }";
        assertEquals(expected, epic.toString(), "Вывод должен соответствовать формату");
    }
}
