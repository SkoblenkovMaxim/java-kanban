package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Task;

import java.util.List;

public interface HistoryManager {
    // Добавление просмотренных задач в список
    void addTask(Task task);

    // Получение последних десяти просмотренных пользователем задач
    List<Task> getHistory();
}
