package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic(List<Integer> subtaskIds, int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int id) {
        subtaskIds.add(id);
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
}
