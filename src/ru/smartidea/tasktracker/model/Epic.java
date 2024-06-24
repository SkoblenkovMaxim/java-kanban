package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;
import ru.smartidea.tasktracker.service.Type;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtaskIds = new ArrayList<>();
    private Type type;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic(List<Integer> subtaskIds, int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.subtaskIds = subtaskIds;
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.type = Type.EPIC;
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
    public String toString() {
        return "ru.smartidea.tasktracker.model.Epic{" +
                "epicTaskId='" + getId() + '\'' +
                ", epicTaskName='" + getName() + '\'' +
                ", epicTaskDescription='" + getDescription() + '\'' +
                ", epicTaskStatus='" + getStatus() + '\'' +
                '}';
    }

//    @Override
    public String toStringFromFile(Epic epic)
    {
        return String.format("%s,%s,%s,%s,%s,%s", epic.getId(), epic.getType(), epic.getName(),
                epic.getStatus(), epic.getDescription(), "");
    }
}
