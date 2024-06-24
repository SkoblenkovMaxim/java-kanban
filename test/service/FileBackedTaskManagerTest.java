package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка FileBackedTaskManager")
public class FileBackedTaskManagerTest {
    private final Path pathTest = Path.of("resources/taskTest.csv");
    private final File fileTest = File.createTempFile("taskTest", null);

    FileBackedTaskManager manager;
    FileBackedTaskManager taskManagerTest;

    public FileBackedTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTaskManager(Managers.getDefaultHistory(), fileTest);
    }

    @AfterEach
    public void afterEach() {
        fileTest.deleteOnExit();
    }

    @Test
    @DisplayName("Сохранение и загрузка пустого файла")
    public void saveAndLoadEmptyFile() {
        manager.save();
        manager = FileBackedTaskManager.loadFromFile(fileTest);

        assertTrue(fileTest.exists());
        assertEquals(Collections.EMPTY_LIST, manager.getAllTask());
    }

    @Test
    @DisplayName("Сохранение и загрузка нескольких задач")
    public void saveAndLoadTasks() {
        Task taskTest1 = new Task(1, "TaskName1", "TaskDescriprion1", TaskStatus.NEW);
        Task taskTest2 = new Task(2, "TaskName2", "TaskDescriprion2", TaskStatus.NEW);

        manager.createTask(taskTest1);
        manager.createTask(taskTest2);
        taskManagerTest = FileBackedTaskManager.loadFromFile(fileTest);
        List<Task> taskList = taskManagerTest.getAllTask();
        List<Epic> epicList = taskManagerTest.getAllEpic();
        Map<Integer, Subtask> subtaskList = taskManagerTest.getAllSubtask();

        assertTrue(fileTest.exists());
        assertEquals(2, taskList.size());
        assertTrue(epicList.isEmpty());
        assertTrue(subtaskList.isEmpty());
    }
}
