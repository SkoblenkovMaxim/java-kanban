package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.HistoryManager;
import ru.smartidea.tasktracker.service.InMemoryHistoryManager;
import ru.smartidea.tasktracker.service.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    Task task = new Task(1,"Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
    Task task2 = new Task(4,"Test2 addNewTask2", "Test2 addNewTask2 description", TaskStatus.NEW);

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    @DisplayName("Проверка добавления задач в историю просмотра")
    void shouldAddTaskFromHistory() {
        Task taskHistoryList1 = historyManager.add(task);
        Task taskHistoryList2 = historyManager.add(task2);
        assertEquals(List.of(taskHistoryList1, taskHistoryList2), historyManager.getHistory());
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
        Task taskHistoryList1 = historyManager.add(task2);
        historyManager.remove(1);

        assertEquals(List.of(taskHistoryList1), historyManager.getHistory());
    }

    @Test
    @DisplayName("Проверка удаления связи")
    void shouldRemoveNodeFromHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task taskTest1 = historyManager.add(task);
        Task taskTest2 = historyManager.add(task2);
        historyManager.remove(taskTest1.getId());

        List<Task> historyListTest = historyManager.getHistory();
        boolean isTaskTest = historyListTest.contains(task);

        assertEquals(historyListTest.size(), 1);
        assertFalse(isTaskTest);
    }
}
