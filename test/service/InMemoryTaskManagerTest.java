package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.InMemoryHistoryManager;
import ru.smartidea.tasktracker.service.InMemoryTaskManager;
import ru.smartidea.tasktracker.service.TaskManager;
import ru.smartidea.tasktracker.service.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("InMemoryTaskManager")
class InMemoryTaskManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);

    Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
    Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", TaskStatus.NEW);
    Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
            TaskStatus.NEW, 1);

    @Test
    @DisplayName("Менеджеры должны совпадать")
    public void shouldBeMatchTaskManager() {
        assertEqualsTaskManager(taskManager, taskManager, "Менеджеры должны совпадать");
        assertEqualsInMemoryTaskManager(taskManager, taskManager, "Менеджеры должны совпадать");
    }

    void assertEqualsTaskManager(TaskManager expected, TaskManager actual, String message) {
        assertEquals(expected, actual, message);
    }

    void assertEqualsInMemoryTaskManager(InMemoryTaskManager expected, InMemoryTaskManager actual, String message) {
        assertEquals(expected, actual, message);
    }

    // Тесты для задач
    @Test
    @DisplayName("Проверка создания задач")
    public void shouldCreateNewTask() {
        taskManager.createTask(task);

        assertNotNull(task.getName());
        assertNotNull(task.getDescription());
        assertNotNull(task.getStatus());
        //assertEquals(1, task.getId(), "Значения должны быть равны 1");
        assertEquals("Test addNewTask", task.getName(), "Значения должны быть равны");
        assertEquals("Test addNewTask description", task.getDescription(), "Значения должны быть равны");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Значения должны быть равны");
    }

    @Test
    @DisplayName("Проверка отсутствия задачи при ее создании")
    public void shouldNotCreateNewTask() {
        Task taskNull = taskManager.createTask(null);
        assertNull(taskNull);
    }

    @Test
    @DisplayName("Проверка получения задачи по ID")
    public void shouldGetTaskId() {
        Task taskTest = taskManager.createTask(task);
        Task taskId = taskManager.getTaskId(taskTest.getId());
        assertEquals(taskTest.getName(), taskId.getName(), "Задачи не совпадают");
    }

    @Test
    @DisplayName("Проверка получения списка задач")
    public void shouldGetListTask() {
        taskManager.createTask(task);
        List<Task> taskList = taskManager.getAllTask();
        assertNotNull(taskList, "Список не найден");
        assertEquals(1, taskList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка получения пустого списка задач")
    public void shouldNotGetListTask() {
        taskManager.createTask(null);
        List<Task> taskList = taskManager.getAllTask();
        assertTrue(taskList.isEmpty(), "Список должен быть пустым");
    }

    @Test
    @DisplayName("Проверка удаления задач по ID")
    public void shouldRemoveTaskById() {
        taskManager.createTask(task);
        taskManager.deleteTask(1);
        assertNull(taskManager.getTaskId(1));
    }

    @Test
    @DisplayName("Проверка удаления отсутствующих задач по ID")
    public void shouldRemoveNullTaskById() {
        taskManager.createTask(task);
        taskManager.deleteTask(10);
        assertNotNull(taskManager.getTaskId(0));
    }

    @Test
    @DisplayName("Проверка удаления всех задач")
    public void shouldRemoveAllTask() {
        taskManager.createTask(task);
        taskManager.removeAllTask();
        List<Task> taskList = taskManager.getAllTask();
        assertTrue(taskList.isEmpty());
    }

    @Test
    @DisplayName("Проверка обновления задач")
    public void shouldUpdateTask() {
        Task taskNew = taskManager.createTask(task);
        taskNew.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(taskNew);
        assertEquals(taskNew.getStatus(), TaskStatus.IN_PROGRESS, "Значения не равны");
    }

    @Test
    @DisplayName("Проверка обновления задач")
    public void shouldNotUpdateTask() {
        Task taskNew = taskManager.createTask(task);
        taskNew.setStatus(null);
        taskManager.updateTask(taskNew);
        assertEquals(taskNew.getStatus(), TaskStatus.NEW, "Значения должны быть равны");
    }

    // Тесты для эпиков
    @Test
    @DisplayName("Проверка создания эпика")
    public void shouldCreateNewEpic() {
        Epic epicTest = taskManager.createEpic(epic);

        assertNotNull(epic.getName());
        assertNotNull(epic.getDescription());
        assertNotNull(epic.getStatus());
        assertEquals(epicTest.getId(), epic.getId(), "ID эпиков должны быть равны");
        assertEquals("Test addNewEpic", epic.getName(), "Значения должны быть равны");
        assertEquals("Test addNewEpic description", epic.getDescription(), "Значения должны быть равны");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Значения должны быть равны");
    }

    @Test
    @DisplayName("Проверка создания null эпика")
    public void shouldNotCreateNewEpic() {
        Task epicNull = taskManager.createEpic(null);

        assertNull(epicNull);
    }

    @Test
    @DisplayName("Проверка получения списка эпиков")
    public void shouldGetListEpic() {
        taskManager.createEpic(epic);
        List<Epic> epicList = taskManager.getAllEpic();
        assertNotNull(epicList, "Список не найден");
        assertEquals(1, epicList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка получения пустого списка эпиков")
    public void shouldGetNullListEpic() {
        taskManager.createEpic(null);
        List<Epic> epicList = taskManager.getAllEpic();
        assertTrue(epicList.isEmpty());
    }

    @Test
    @DisplayName("Проверка удаления эпика по ID")
    public void shouldRemoveEpicById() {
        Epic epicTest = taskManager.createEpic(epic);
        taskManager.deleteEpic(epic.getId());
        List<Epic> epicTest1 = taskManager.getAllEpic();
        assertTrue(epicTest1.isEmpty());
    }

    @Test
    @DisplayName("Проверка удаления всех эпиков")
    public void shouldRemoveAllEpic() {
        taskManager.createEpic(epic);
        taskManager.removeAllEpic();
        List<Epic> taskEpic = taskManager.getAllEpic();
        assertTrue(taskEpic.isEmpty());
    }

    @Test
    @DisplayName("Проверка обновления эпиков (статуса эпиков)")
    public void shouldUpdateEpic() {
        Epic epicUpdate = taskManager.createEpic(epic);
        epic.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpicStatus(epic);
        taskManager.updateEpic(epic);
        assertEquals(epicUpdate.getStatus(), taskManager.getEpicId(epicUpdate.getId()).getStatus(),
                "Значения не равны");
    }

    // Тесты для подзадач
    @Test
    @DisplayName("Проверка создания подзадач эпика")
    public void shouldCreateSubtaskForEpic() {
        Epic epicWithSubtask = taskManager.createEpic(epic);
        Subtask subtaskForEpic = taskManager.createSubtask(subtask);

        Map<Integer, Subtask> mapSubtaskTest = taskManager.getAllSubtask(); // Получение всех подзадач
        List<Task> listOfSubtask = new ArrayList<>(mapSubtaskTest.values());
        listOfSubtask.add(subtaskForEpic);

        assertEquals(subtaskForEpic.getStatus(), subtask.getStatus());
        assertEquals(List.of(subtaskForEpic), listOfSubtask);
        epicWithSubtask.setSubtaskIds(subtaskForEpic.getId());
        assertEquals(List.of(subtaskForEpic.getId()), epicWithSubtask.getSubtaskIds());
    }

    @Test
    @DisplayName("Проверка создания подзадач эпика")
    public void shouldNotCreateSubtaskForEpic() {
        taskManager.createEpic(null);
        Subtask subtaskForEpic = taskManager.createSubtask(null);

        Map<Integer, Subtask> mapSubtaskTest = taskManager.getAllSubtask(); // Получение всех подзадач
        List<Task> listOfSubtask = new ArrayList<>(mapSubtaskTest.values());

        assertNull(subtaskForEpic);
        assertTrue(listOfSubtask.isEmpty());
    }

    @Test
    @DisplayName("Проверка удаления подзадач")
    public void shouldRemoveAllSubtask() {
        taskManager.createSubtask(subtask);
        taskManager.removeAllSubtask();
        Map<Integer, Subtask> mapSubtaskTest = taskManager.getAllSubtask();
        assertEquals(0, mapSubtaskTest.size());
    }

    @Test
    @DisplayName("Проверка получения подзадачи по ID")
    public void shouldGetSubtaskId() {
        taskManager.createEpic(epic);
        Subtask subtaskTest = taskManager.createSubtask(subtask);
        Map<Integer, Subtask> mapSubtaskTest = taskManager.getAllSubtask();
        List<Subtask> subtaskListTest = new ArrayList<>(mapSubtaskTest.values());
        subtaskListTest.add(subtaskTest);
        assertNotNull(subtaskTest.getStatus());
        assertEquals(List.of(subtaskTest), subtaskListTest);
    }

    @Test
    @DisplayName("Проверка получения подзадачи по ID")
    public void shouldNotGetSubtaskId() {
        taskManager.createSubtask(subtask);
        Subtask subtaskId = taskManager.getSubtaskId(30);
        assertNull(subtaskId);
    }

    @Test
    @DisplayName("Проверка обновления подзадач")
    public void shouldUpdateSubtask() {
        taskManager.createEpic(epic);
        Subtask subtaskUpdate = taskManager.createSubtask(subtask);
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        assertEquals(TaskStatus.IN_PROGRESS, subtaskUpdate.getStatus(),
                "Значения не равны");
    }

    @Test
    @DisplayName("Проверка обновления подзадач")
    public void shouldNotUpdateSubtask() {
        taskManager.createEpic(epic);
        Subtask subtaskUpdate = taskManager.createSubtask(subtask);
        subtask.setStatus(null);
        taskManager.updateSubtask(subtaskUpdate);
        assertEquals(TaskStatus.NEW, subtaskUpdate.getStatus(),
                "Значения должны быть NEW");
    }

    @Test
    @DisplayName("Проверка удаления подзадач по ID")
    public void shouldRemoveSubtaskById() {
        Epic epicWithSubtask = taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtask(subtask.getId(), epicWithSubtask);
        List<Task> taskList = taskManager.getAllTask();
        //assertEquals(0, taskList.size(), "Неверное количество задач.");
        assertTrue(taskList.isEmpty());
    }

    @Test
    @DisplayName("Проверка истории просмотра")
    public void shouldGetHistory() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Task taskId = taskManager.getTaskId(epic.getId());
        Task epicId = taskManager.getEpicId(epic.getId());
        Task subtaskId = taskManager.getSubtaskId(subtask.getId());

        historyManager.add(taskId);
        historyManager.add(epicId);
        historyManager.add(subtaskId);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
    }
}
