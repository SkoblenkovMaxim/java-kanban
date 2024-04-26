package test.service;

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

@DisplayName("InMemoryTaskManager")
class InMemoryTaskManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);

    Task task = new Task(1,"Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
    Epic epic = new Epic(2,"Test addNewEpic", "Test addNewEpic description", TaskStatus.NEW);
    Subtask subtask = new Subtask(3,"Test addNewSubtask", "Test addNewSubtask description",
            TaskStatus.NEW, 2);

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
        assertEquals(1, task.getId(), "Значения должны быть равны 1");
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
        Task taskId = taskManager.getTaskId(1);
        assertEquals(taskTest.getName(), taskId.getName(), "Задачи не совпадают");
    }

    @Test
    @DisplayName("Проверка получения null по ID задачи")
    public void shouldGetNullTaskId() {
        taskManager.createTask(null);
        Task taskId = taskManager.getTaskId(0);
        assertNull(taskId, "Должно быть null");
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
        assertNull(taskList.get(0), "Список должен быть пустым");
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
        assertEquals(0, taskList.size(), "Неверное количество задач.");
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
        taskManager.createEpic(epic);

        assertNotNull(epic.getName());
        assertNotNull(epic.getDescription());
        assertNotNull(epic.getStatus());
        assertEquals(2, epic.getId(), "ID эпиков должны быть равны");
        assertEquals("Test addNewEpic", epic.getName(), "Значения должны быть равны");
        assertEquals("Test addNewEpic description", epic.getDescription(), "Значения должны быть равны");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Значения должны быть равны");
    }

    @Test
    @DisplayName("Проверка создания эпика")
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
        assertNull(epicList.get(0));
    }

    @Test
    @DisplayName("Проверка удаления эпика по ID")
    public void shouldRemoveEpicById() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        List<Integer> subtaskIds = new ArrayList<>();
        subtaskIds.add(subtask.getId());
        taskManager.deleteEpic(epic.getId(), subtaskIds);
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(0, taskList.size(), "Неверное количество эпиков.");
    }

    @Test
    @DisplayName("Проверка удаления null эпика по ID")
    public void shouldNotRemoveEpicById() {
        taskManager.createEpic(null);
        taskManager.createSubtask(null);
        List<Integer> subtaskIds = new ArrayList<>();
        subtaskIds.add(null);
        taskManager.deleteEpic(epic.getId(), subtaskIds);
        List<Task> taskList = taskManager.getAllTask();
        assertNull(taskList);
    }

    @Test
    @DisplayName("Проверка удаления всех эпиков")
    public void shouldRemoveAllEpic() {
        taskManager.createEpic(epic);
        taskManager.removeAllEpic();
        List<Epic> taskEpic = taskManager.getAllEpic();
        assertEquals(0, taskEpic.size(), "Неверное количество эпиков.");
    }

    @Test
    @DisplayName("Проверка обновления эпиков (статуса эпиков)")
    public void shouldUpdateEpic() {
        Epic epicUpdate = taskManager.createEpic(epic);
        epic.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpicStatus(epic);
        taskManager.updateEpic(epic);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskId(epicUpdate.getId()).getStatus(),
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

        assertNotNull(subtaskForEpic.getName());
        assertNotNull(subtaskForEpic.getDescription());
        assertNotNull(subtaskForEpic.getStatus());
        assertEquals(TaskStatus.NEW, subtaskForEpic.getStatus());
        assertEquals(epicWithSubtask.getId(), subtaskForEpic.getEpicId());
        assertEquals(List.of(subtaskForEpic), listOfSubtask);
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
        Subtask subtaskForEpic = taskManager.createSubtask(subtask);
        taskManager.removeAllSubtask(epic);
        assertNull(subtaskForEpic);
    }

    @Test
    @DisplayName("Проверка получения подзадачи по ID")
    public void shouldGetSubtaskId() {
        taskManager.createSubtask(subtask);
        Subtask subtaskId = taskManager.getSubtaskId(3);
        assertEquals(3, subtaskId.getId(), "Задачи не совпадают");
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
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskId(subtaskUpdate.getId()).getStatus(),
                "Значения не равны");
    }

    @Test
    @DisplayName("Проверка обновления подзадач")
    public void shouldNotUpdateSubtask() {
        Epic epicNew = taskManager.createEpic(epic);
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
        assertEquals(0, taskList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка удаления подзадач по ID")
    public void shouldNotRemoveSubtaskById() {
        Epic epicWithSubtask = taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtask(10, epicWithSubtask);
        List<Task> taskList = taskManager.getAllTask();
        assertEquals(1, taskList.size(), "Количество задач должно быть 1.");
    }

    @Test
    @DisplayName("Проверка истории просмотра")
    public void shouldGetHistory() {
        Task taskHistory = taskManager.createTask(task);
        Epic epicHistory = taskManager.createEpic(epic);
        Subtask subtaskHistory = taskManager.createSubtask(subtask);

        Task taskId = taskManager.getTaskId(epic.getId());
        Task epicId = taskManager.getEpicId(epic.getId());
        Task subtaskId = taskManager.getSubtaskId(subtask.getId());

        historyManager.addTask(taskId);
        historyManager.addTask(epicId);
        historyManager.addTask(subtaskId);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
    }
}
