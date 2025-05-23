import managers.impl.FileBackedTaskManager;
import managers.impl.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void setup() throws IOException {
        taskManager = new InMemoryTaskManager();
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return (FileBackedTaskManager) taskManager;
    }

}
