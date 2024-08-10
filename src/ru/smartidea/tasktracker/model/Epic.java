package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;
import ru.smartidea.tasktracker.service.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, TaskStatus.NEW, startTime, duration);
    }

    public Epic(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
    }

    public Epic(List<Integer> subtaskIds, int id, String name, String description, TaskStatus status,
                LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.subtaskIds = subtaskIds;
    }

    public Epic(int id, String name, TaskStatus status, String description, LocalDateTime startTime,
                Duration duration) {
        super(id, name, description, status, startTime, duration);
    }

    public Epic() {
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int id) {
        subtaskIds.add(id);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "ru.smartidea.tasktracker.model.Epic{" +
                "epicTaskId='" + getId() + '\'' +
                ", epicTaskName='" + getName() + '\'' +
                ", epicTaskDescription='" + getDescription() + '\'' +
                ", epicTaskStatus='" + getStatus() + '\'' +
                '}';
    }

    public String toStringFromFile(Epic epic) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", epic.getId(), epic.getType(), epic.getName(),
                epic.getStatus(), epic.getDescription(), epic.getStartTime(), epic.getEndTime(), "");
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
