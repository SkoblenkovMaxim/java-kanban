package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected static int id = 0;

    HashMap<Integer, Task> taskOrdinaryMap = new HashMap<>(); // Мапа для хранения задач
    HashMap<Integer, Epic> taskEpicMap = new HashMap<>(); // Мапа для хранения эпиков
    HashMap<Integer, Subtask> taskSubMap = new HashMap<>(); // Мапа для хранения подзадач
    HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private static int idIncrease() {
        return id++;
    }

    // Задачи
    // Создание задач
    @Override
    public Task createTask(Task task) {
        int taskId = idIncrease();
        taskOrdinaryMap.put(taskId, task);
        return task;
    }

    // Получение по идентификатору задачи
    @Override
    public Task getTaskId(Integer newTaskId) {
        Task task = taskOrdinaryMap.get(newTaskId);
        historyManager.addTask(task);
        return task;
    }

    // Получение списка задач
    @Override
    public List<Task> getAllTask() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(taskOrdinaryMap.values());
        return allTasks;
    }

    // Удаление задачи по id
    @Override
    public void deleteTask(int idTask) {
        taskOrdinaryMap.remove(idTask);
    }

    // Удаление всех задач
    @Override
    public void removeAllTask() {
        taskOrdinaryMap.clear();
    }

    // Обновление задач
    @Override
    public HashMap<Integer, Task> updateTask(Task task) {
        taskOrdinaryMap.put(task.getId(), task);
        return taskOrdinaryMap;
    }

    // Эпики
    // Создание эпика
    @Override
    public Epic createEpic(Epic epic) {
        int taskId = idIncrease();
        taskEpicMap.put(taskId, epic);
        return epic;
    }

    // Получение по идентификатору эпик задачи
    @Override
    public Epic getEpicId(Integer newTaskId) {
        Epic epic = taskEpicMap.get(newTaskId);
        if (epic != null && taskEpicMap.containsKey(newTaskId)) {
            historyManager.addTask(epic);
        } else {
            return null;
        }
        return  epic;
    }

    // Получение всех эпик задач
    @Override
    public List<Epic> getAllEpic() {
        List<Epic> allEpics = new ArrayList<>();
        allEpics.addAll(taskEpicMap.values());
        return allEpics;
    }

    // Удаление эпика
    @Override
    public void deleteEpic(int idTask, List<Integer> subtaskIds) {
        taskEpicMap.remove(idTask);
        taskSubMap.remove(subtaskIds);
        historyManager.getHistory().remove(idTask);
    }

    // Удаление всех эпик задач
    @Override
    public void removeAllEpic() {
        taskEpicMap.clear();
        taskSubMap.clear();
    }

    // Обновление эпика
    @Override
    public HashMap<Integer, Epic> updateEpic(Epic epic) {
        if (taskEpicMap.containsKey(epic.getId())) {
            taskEpicMap.put(epic.getId(), epic);
        }
        return taskEpicMap;
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
            TaskStatus status = taskSubMap.get(epicSub).getStatus();
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
        int taskId = idIncrease();
        subtask.setId(taskId);
        Epic epic = taskEpicMap.get(subtask.getEpicId());
        if (epic != null) {
            taskSubMap.put(taskId, subtask);
            epic.setSubtaskIds(taskId);
            updateEpicStatus(epic);
        } else {
            return null;
        }
        return subtask;
    }

    // Получение всех подзадач
    @Override
    public HashMap<Integer, Subtask> getAllSubtask() {
        return taskSubMap;
    }

    // Удаление всех подзадач
    @Override
    public void removeAllSubtask(Epic epic) {
        taskSubMap.clear();
        updateEpicStatus(epic);
    }

    // Получение по идентификатору подзадачи
    @Override
    public Subtask getSubtaskId(Integer newTaskId) {
        Subtask subtask = taskSubMap.get(newTaskId);
        historyManager.addTask(subtask);
        return  subtask;
    }

    // Обновление подзадач
    @Override
    public HashMap<Integer, Subtask> updateSubtask(Subtask subtask) {
        if (taskSubMap.containsKey(subtask.getId())) {
            taskSubMap.put(subtask.getId(), subtask);
        }
        return taskSubMap;
    }

    // Удаление подзадач по ID
    @Override
    public void deleteSubtask(int id, Epic epic) {
        taskSubMap.remove(id);
        updateEpicStatus(epic);
        historyManager.getHistory().remove(id);
    }
}
