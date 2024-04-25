package ru.smartidea.tasktracker.service;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    // Возвращает историю просмотров
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
