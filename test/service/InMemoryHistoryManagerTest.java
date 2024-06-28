package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.HistoryManager;
import ru.smartidea.tasktracker.service.InMemoryHistoryManager;
import ru.smartidea.tasktracker.service.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    Task task = new Task(1,"Test addNewTask", "Test addNewTask description", TaskStatus.NEW,
            LocalDateTime.now(), Duration.ofMinutes(0));
    Task task2 = new Task(4,"Test2 addNewTask2", "Test2 addNewTask2 description", TaskStatus.NEW,
            LocalDateTime.now(), Duration.ofMinutes(0));

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
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.remove(1);

        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Проверка удаления связи")
    void shouldRemoveNodeFromHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.remove(historyManager.getHistory().indexOf(task));

        List<Task> historyListTest = historyManager.getHistory();
        boolean isTaskTest = historyListTest.contains(task);

        assertEquals(historyListTest.size(), 1);
        assertTrue(isTaskTest);
    }
}
