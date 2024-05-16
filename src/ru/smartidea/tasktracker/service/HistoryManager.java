package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Task;

import java.util.List;

public interface HistoryManager {
    // Добавление просмотренных задач в список
    Task addTask(Task task);

    // Удаление задачи из просмотра
    void remove(int id);

    // Получение последних десяти просмотренных пользователем задач
    List<Task> getHistory();
}
