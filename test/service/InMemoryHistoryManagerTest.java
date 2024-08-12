package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private final TaskManager taskManager = new InMemoryTaskManager(Managers.getDefaultHistory());

    Task task = new Task(1,"Test addNewTask", "Test addNewTask description", TaskStatus.NEW,
            LocalDateTime.now(), Duration.ofMinutes(0));
    Task task2 = new Task(4,"Test2 addNewTask2", "Test2 addNewTask2 description", TaskStatus.NEW,
            LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(0));

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    @DisplayName("Проверка добавления задач в историю просмотра")
    void shouldAddTaskFromHistory() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        historyManager.add(task2);
        history = historyManager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    @DisplayName("Проверка добавления пустой задачи")
    void shouldReturnNullIfTaskIsEmpty() {
        historyManager.add(null);

        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Проверка удаления задачи из истории просмотра")
    void shouldRemoveTaskFromHistory() {
        Task t1 = taskManager.createTask(task);
        Task t2 = taskManager.createTask(task2);
        historyManager.add(t1);
        historyManager.add(t2);
        historyManager.remove(t1.getId());

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Проверка удаления связи")
    void shouldRemoveNodeFromHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        taskManager.createTask(task);
        taskManager.createTask(task2);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.remove(historyManager.getHistory().indexOf(task));

        List<Task> historyListTest = historyManager.getHistory();
        boolean isTaskTest = historyListTest.contains(task2);

        assertEquals(historyListTest.size(), 1);
        assertTrue(isTaskTest);
    }
}
