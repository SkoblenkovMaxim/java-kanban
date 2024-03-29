package ru.smartidea.tasktracker.service;

public enum TaskStatus {
    NEW, // Задача только создана, но к её выполнению ещё не приступили
    IN_PROGRESS, // Над задачей ведётся работа
    DONE // Задача выполнена
}
