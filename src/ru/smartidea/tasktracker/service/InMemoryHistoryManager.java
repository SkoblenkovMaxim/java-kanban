package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
//    InMemoryTaskManager inMemoryTaskManager;
    static List<Task> historyTask = new ArrayList<>();

    public InMemoryHistoryManager() {
        getHistory();
    }

    // Добавление просмотренных задач
    @Override
    public void addTask(Task task) {
        // Условие для проверки размера
        if (historyTask.size() > 10) {
            historyTask.remove(0);
        }
        historyTask.add(task);
    }

    // Получение последних десяти просмотренных пользователем задач
    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
