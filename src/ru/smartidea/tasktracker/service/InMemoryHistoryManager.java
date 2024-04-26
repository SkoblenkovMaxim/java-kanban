package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
//    InMemoryTaskManager inMemoryTaskManager;
    private List<Task> historyTask = new ArrayList<>();

    public List<Task> getHistoryTask() {
        return historyTask;
    }

    public void setHistoryTask(List<Task> historyTask) {
        this.historyTask = historyTask;
    }

    public InMemoryHistoryManager() {
        getHistory();
    }

    // Добавление просмотренных задач
    @Override
    public void addTask(Task task) {
        // Условие для проверки размера
        if (getHistoryTask().size() > 10) {
            getHistoryTask().remove(0);
        }
        getHistoryTask().add(task);
    }

    // Получение последних десяти просмотренных пользователем задач
    @Override
    public List<Task> getHistory() {
        return getHistoryTask();
    }
}
