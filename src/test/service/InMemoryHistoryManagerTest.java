package test.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.HistoryManager;
import ru.smartidea.tasktracker.service.InMemoryHistoryManager;
import ru.smartidea.tasktracker.service.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    Task task = new Task(1,"Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
    Epic epic = new Epic(2,"Test addNewEpic", "Test addNewEpic description", TaskStatus.NEW);
    Subtask subtask = new Subtask(3,"Test addNewSubtask", "Test addNewSubtask description",
            TaskStatus.NEW, 2);
    Task task2 = new Task(4,"Test2 addNewTask2", "Test2 addNewTask2 description", TaskStatus.NEW);

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    @DisplayName("Проверка добавления задач в историю просмотра")
    void shouldAddTaskFromHistory() {
        Task taskHistoryList1 = historyManager.addTask(task);
        Task taskHistoryList2 = historyManager.addTask(task2);
        assertEquals(List.of(taskHistoryList1, taskHistoryList2), historyManager.getHistory());
    }

    @Test
    @DisplayName("Проверка добавления пустой задачи")
    void shouldReturnNullIfTaskIsEmpty() {
        historyManager.addTask(null);

        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Проверка удаления задачи из истории просмотра")
    void shouldRemoveTaskFromHistory() {
        historyManager.addTask(task);
        Task taskHistoryList1 = historyManager.addTask(task2);
        historyManager.remove(1);

        assertEquals(List.of(taskHistoryList1), historyManager.getHistory());
    }
}
