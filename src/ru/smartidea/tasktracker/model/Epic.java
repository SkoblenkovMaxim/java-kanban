package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubtaskIds() {
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
