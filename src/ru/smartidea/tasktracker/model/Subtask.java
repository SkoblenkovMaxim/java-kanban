package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;
import ru.smartidea.tasktracker.service.Type;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(int id, String name, String description, TaskStatus status, LocalDateTime startTime,
                   Duration duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name,
                   String description,
                   TaskStatus status,
                   LocalDateTime startTime,
                   Duration duration,
                   int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, TaskStatus status, String description, int epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
    }

    public Subtask(int epicId, String name, String description, int id, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
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

//    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", getId(), getType(), getName(),
                getStatus(), getDescription(), getStartTime(), getEndTime(), getDuration().toMinutes(), getEpicId());
    }
}
