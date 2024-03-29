package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int taskId = 0; // Идентификатор задачи

    HashMap<Integer, Task> taskOrdinaryMap = new HashMap<>();
    HashMap<Integer, Epic> taskEpicMap = new HashMap<>();
    HashMap<Integer, Subtask> taskSubMap = new HashMap<>();
    ArrayList<Integer> subtaskIds = new ArrayList<>(taskSubMap.keySet());

    // Получение списка задач
    public HashMap<Integer, Task> getAllTask() {
        return taskOrdinaryMap;
    }
    // Получение всех эпик задач
    public HashMap<Integer, Epic> getAllEpic() {
        return taskEpicMap;
    }
    // Получение всех подзадач
    public HashMap<Integer, Subtask> getAllSubtask() {
        return taskSubMap;
    }

    // Удаление всех задач
    public void removeAllTask() {
        taskOrdinaryMap.clear();
    }

    // Удаление всех эпик задач
    public void removeAllEpic() {
        taskEpicMap.clear();
        taskSubMap.clear();
    }

    // Удаление всех подзадач
    public void removeAllSubtask(Epic epic) {
        taskSubMap.clear();
        updateEpicStatus(epic);
    }

    // Получение по идентификатору задачи
    public void getTaskId(Integer newTaskId) {
        taskOrdinaryMap.get(newTaskId);
    }

    // Получение по идентификатору эпик задачи
    public void getEpicId(Integer newTaskId) {
        taskEpicMap.get(newTaskId);
    }

    // Получение по идентификатору подзадачи
    public void getSubtask(Integer newTaskId) {
        taskSubMap.get(newTaskId);
    }

    // Создание задачи
    public void createTask(String taskName, String taskDescription, TaskStatus taskStatus) {
        taskId++;
        int taskId = TaskManager.taskId;
        Task task = new Task(taskId, taskName, taskDescription, taskStatus);
        taskOrdinaryMap.put(taskId, task);
    }

    // Создание эпик
    public void createEpic(String taskName, String taskDescription, TaskStatus taskStatus) {
        taskId++;
        int taskId = TaskManager.taskId;
        taskEpicMap.put(taskId, new Epic(taskId, taskName, taskDescription, taskStatus));
        subtaskIds = new ArrayList<>();
    }

    // Создание подзадачи
    public void createSubtask(String taskName, String taskDescription, TaskStatus taskStatus, int epicId) {
        Epic epic = taskEpicMap.get(epicId);
        Subtask subtask = new Subtask(taskId, taskName, taskDescription, taskStatus, epic, epicId);
        taskId++;
        int taskId = TaskManager.taskId;
        subtaskIds.add(taskId);
        taskSubMap.put(taskId, subtask);
        updateEpicStatus(epic);
    }

    // Обновление задач
    public HashMap<Integer, Task> updateTask(Task task) {
        if (taskOrdinaryMap.containsKey(task.getId())) {
            taskOrdinaryMap.put(task.getId(), task);
        }
        return taskOrdinaryMap;
    }

    // Обновление эпика
    public HashMap<Integer, Epic> updateEpic(Epic epic) {
        if (taskEpicMap.containsKey(epic.getId())) {
            taskEpicMap.put(epic.getId(), epic);
        }
        return taskEpicMap;
    }

    // Обновление подзадач
    public HashMap<Integer, Subtask> updateSubtask(Subtask subtask) {
        if (taskSubMap.containsKey(subtask.getId())) {
            taskSubMap.put(subtask.getId(), subtask);
        }
        return taskSubMap;
    }

    // Удаление задач
    public void deleteTask(int idTask) {
        taskOrdinaryMap.remove(idTask);
    }

    // Удаление эпика
    public void deleteEpic(int idTask, ArrayList<Integer> subtaskIds) {
        taskEpicMap.remove(idTask);
        taskSubMap.remove(subtaskIds);
    }

    // Удаление подзадач
    public void deleteSubtask(int id, Epic epic) {
        taskSubMap.remove(id);
        updateEpicStatus(epic);
    }

    // Обновление статуса эпика
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
}
