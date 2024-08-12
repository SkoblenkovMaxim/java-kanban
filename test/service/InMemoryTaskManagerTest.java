package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ru.smartidea.tasktracker.service.*;

@DisplayName("InMemoryTaskManager")
class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }
}
