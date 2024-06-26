package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    // Получение списка задач
    List<Task> getAllTask();

    // Получение всех эпик задач
    List<Epic> getAllEpic();

    // Получение всех подзадач
    Map<Integer, Subtask> getAllSubtask();

    // Удаление всех задач
    void removeAllTask();

    // Удаление всех эпик задач
    void removeAllEpic();

    // Удаление всех подзадач
    void removeAllSubtask();

    // Получение по идентификатору задачи
    Task getTaskId(Integer newTaskId);

    // Получение по идентификатору эпик задачи
    Epic getEpicId(Integer newTaskId);

    // Получение по идентификатору подзадачи
    Subtask getSubtaskId(Integer newTaskId);

    // Создание задачи
    Task createTask(Task task);

    // Создание эпик
    Epic createEpic(Epic epic);

    // Создание подзадачи
    Subtask createSubtask(Subtask subtask);

    // Обновление задач
    Map<Integer, Task> updateTask(Task task);

    // Обновление эпика
    Map<Integer, Epic> updateEpic(Epic epic);

    // Обновление подзадач
    Map<Integer, Subtask> updateSubtask(Subtask subtask);

    // Удаление задач
    void deleteTask(int idTask);

    // Удаление эпика
    void deleteEpic(int idTask);

    // Удаление подзадач
    void deleteSubtask(int id, Epic epic);

    // Обновление статуса эпика
    void updateEpicStatus(Epic epic);

    // Получение истории
    List<Task> getHistory();
}
