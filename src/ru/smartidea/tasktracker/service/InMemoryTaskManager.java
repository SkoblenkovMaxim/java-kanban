package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected int id;

    private Map<Integer, Task> taskOrdinaryMap = new HashMap<>(); // Мапа для хранения задач
    private Map<Integer, Epic> taskEpicMap = new HashMap<>(); // Мапа для хранения эпиков
    private Map<Integer, Subtask> taskSubMap = new HashMap<>(); // Мапа для хранения подзадач
    HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
        int id = 0;
        historyManager = Managers.getDefaultHistory();
    }

    public Map<Integer, Task> getTaskOrdinaryMap() {
        return taskOrdinaryMap;
    }

    public void setTaskOrdinaryMap(Map<Integer, Task> taskOrdinaryMap) {
        this.taskOrdinaryMap = taskOrdinaryMap;
    }

    public Map<Integer, Epic> getTaskEpicMap() {
        return taskEpicMap;
    }

    public void setTaskEpicMap(Map<Integer, Epic> taskEpicMap) {
        this.taskEpicMap = taskEpicMap;
    }

    public Map<Integer, Subtask> getTaskSubMap() {
        return taskSubMap;
    }

    public void setTaskSubMap(Map<Integer, Subtask> taskSubMap) {
        this.taskSubMap = taskSubMap;
    }

    int idIncrease() {
        return id++;
    }

    // Задачи
    // Создание задач
    @Override
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        } else {
            int taskId = idIncrease();
            task.setId(taskId);
            getTaskOrdinaryMap().put(taskId, task);
            return task;
        }
    }

    // Получение по идентификатору задачи
    @Override
    public Task getTaskId(Integer newTaskId) {
        Task task = getTaskOrdinaryMap().get(newTaskId);
        if (task == null) {
            return null;
        } else {
            historyManager.add(task);
            return task;
        }
    }

    // Получение списка задач
    @Override
    public List<Task> getAllTask() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTaskOrdinaryMap().values());
        if (allTasks.size() == 0) {
            return null;
        } else {
            return allTasks;
        }
    }

    // Удаление задачи по id
    @Override
    public void deleteTask(int idTask) {
        getTaskOrdinaryMap().remove(idTask);
    }

    // Удаление всех задач
    @Override
    public void removeAllTask() {
        getTaskOrdinaryMap().clear();
    }

    // Обновление задач
    @Override
    public Map<Integer, Task> updateTask(Task task) {
        getTaskOrdinaryMap().put(task.getId(), task);
        return getTaskOrdinaryMap();
    }

    // Эпики
    // Создание эпика
    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        } else {
            int taskId = idIncrease();
            epic.setId(taskId);
            getTaskEpicMap().put(taskId, epic);
            return epic;
        }
    }

    // Получение по идентификатору эпик задачи
    @Override
    public Epic getEpicId(Integer newTaskId) {
        Epic epic = getTaskEpicMap().get(newTaskId);
        if (epic != null && getTaskEpicMap().containsKey(newTaskId)) {
            historyManager.add(epic);
        } else {
            return null;
        }
        return  epic;
    }

    // Получение всех эпик задач
    @Override
    public List<Epic> getAllEpic() {
        List<Epic> allEpics = new ArrayList<>();
        allEpics.addAll(getTaskEpicMap().values());
        if (allEpics.size() == 0) {
            return null;
        } else {
            return allEpics;
        }
    }

    // Удаление эпика
    @Override
    public void deleteEpic(int idTask) {
        if (getTaskEpicMap().containsKey(idTask)) {
            Epic epic = getTaskEpicMap().get(idTask);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                historyManager.remove(idTask);
                getTaskSubMap().remove(subtaskId);
            }
            getTaskEpicMap().remove(idTask);
            historyManager.remove(epic.getId());
        }
    }

    // Удаление всех эпик задач
    @Override
    public void removeAllEpic() {
        getTaskEpicMap().clear();
        getTaskSubMap().clear();
    }

    // Обновление эпика
    @Override
    public Map<Integer, Epic> updateEpic(Epic epic) {
        if (getTaskEpicMap().containsKey(epic.getId())) {
            getTaskEpicMap().put(epic.getId(), epic);
        }
        return getTaskEpicMap();
    }

    // Обновление статуса эпика
    @Override
    public void updateEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;

        if (epic.getSubtaskIds().size() == 0) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        for (Integer epicSub : epic.getSubtaskIds()) {
            TaskStatus status = getTaskSubMap().get(epicSub).getStatus();
            if (status != TaskStatus.NEW) {
                isNew = false;
            }
            if (status != TaskStatus.DONE) {
                isDone = false;
            }
        }
        if (isNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (isDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    // Подзадачи
    // Создание подзадач
    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask == null) {
            return null;
        } else {
            int taskId = idIncrease();
            subtask.setId(taskId);
            Epic epic = getTaskEpicMap().get(subtask.getEpicId());
            if (epic != null) {
                getTaskSubMap().put(taskId, subtask);
                epic.setSubtaskIds(taskId);
                updateEpicStatus(epic);
            }
            return subtask;
        }
    }

    // Получение всех подзадач
    @Override
    public Map<Integer, Subtask> getAllSubtask() {
        return getTaskSubMap();
    }

    // Удаление всех подзадач
    @Override
    public void removeAllSubtask() {
        for (Epic epic1 : getTaskEpicMap().values()) {
            for (Integer subtaskId : epic1.getSubtaskIds()) {
                getTaskSubMap().remove(subtaskId);
                historyManager.remove(subtaskId);
                getTaskSubMap().get(subtaskId);
            }
            epic1.getSubtaskIds().clear();
        }
    }

    // Получение по идентификатору подзадачи
    @Override
    public Subtask getSubtaskId(Integer newTaskId) {
        Subtask subtask = getTaskSubMap().get(newTaskId);
        historyManager.add(subtask);
        return  subtask;
    }

    // Обновление подзадач
    @Override
    public Map<Integer, Subtask> updateSubtask(Subtask subtask) {
        if (getTaskSubMap().containsKey(subtask.getId())) {
            getTaskSubMap().put(subtask.getId(), subtask);
        }
        return getTaskSubMap();
    }

    // Удаление подзадач по ID
    @Override
    public void deleteSubtask(int id, Epic epic) {
        if (getTaskSubMap().containsKey(id)) {
            getTaskSubMap().remove(id);
            updateEpicStatus(epic);
            historyManager.getHistory().remove(id);
        }
    }
}
