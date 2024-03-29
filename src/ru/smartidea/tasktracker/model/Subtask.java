package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;

public class Subtask extends Task {
    Epic epic;
    protected int epicId;
    public Subtask(int id, String name, String description, TaskStatus status, Epic epic, int epicId) {
        super(id, name, description, status);
        this.epic = epic;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "ru.smartidea.tasktracker.model.Subtask{" +
                "epicId='" + epicId + '\'' +
                ", subtaskId='" + getId() + '\'' +
                ", subtaskName='" + getName() + '\'' +
                ", subtaskDescription='" + getDescription() + '\'' +
                ", subtaskStatus='" + getStatus() + '\'' +
                '}';
    }
}
