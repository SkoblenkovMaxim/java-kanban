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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка FileBackedTaskManager")
public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File fileTest;

    @BeforeEach
    public void beforeEach() throws IOException {
        fileTest = File.createTempFile("taskTest", "csv");
        taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), fileTest);
    }

    @AfterEach
    public void afterEach() {
        fileTest.deleteOnExit();
    }

    @Test
    @DisplayName("Сохранение и загрузка пустого файла")
    public void saveAndLoadEmptyFile() {
        taskManager.save();
        taskManager = FileBackedTaskManager.loadFromFile(fileTest);

        assertTrue(fileTest.exists());
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllTask());
    }

    @Test
    @DisplayName("не загружается когда нет файла для загрузки")
    void whenLoadTasksAndFileNotExists() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(new File("Path/notExistPath"));
        }, "Не удалось загрузить данные");
    }

    @Test
    @DisplayName("Сохранение и загрузка нескольких задач")
    public void saveAndLoadTasks() {
        Task taskTest1 = new Task(1, "TaskName1", "TaskDescriprion1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(0));
        Task taskTest2 = new Task(2, "TaskName2", "TaskDescriprion2", TaskStatus.NEW,
                LocalDateTime.now().plusMinutes(5), Duration.ofMinutes(5));

        taskManager.createTask(taskTest1);
        taskManager.createTask(taskTest2);
        taskManager = FileBackedTaskManager.loadFromFile(fileTest);
        List<Task> taskList = taskManager.getAllTask();
        List<Epic> epicList = taskManager.getAllEpic();
        Map<Integer, Subtask> subtaskList = taskManager.getAllSubtask();

        assertTrue(fileTest.exists());
        assertEquals(2, taskList.size());
        assertTrue(epicList.isEmpty());
        assertTrue(subtaskList.isEmpty());
    }
}
