package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "ru.smartidea.tasktracker.model.Subtask{" +
                "epicId='" + getEpicId() + '\'' +
                ", subtaskId='" + getId() + '\'' +
                ", subtaskName='" + getName() + '\'' +
                ", subtaskDescription='" + getDescription() + '\'' +
                ", subtaskStatus='" + getStatus() + '\'' +
                '}';
    }
}
