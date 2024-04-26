package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(int id, String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String taskName) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        if (status == TaskStatus.NEW) {
            return TaskStatus.NEW;
        } else if (status == TaskStatus.DONE) {
            return TaskStatus.DONE;
        } else if (status == TaskStatus.IN_PROGRESS) {
            return TaskStatus.IN_PROGRESS;
        } else {
            return null;
        }
    }

    public void setStatus(TaskStatus status) {
        if (status == TaskStatus.NEW) {
            this.status = TaskStatus.NEW;
        } else if (status == TaskStatus.DONE) {
            this.status = TaskStatus.DONE;
        } else if (status == TaskStatus.IN_PROGRESS) {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "ru.smartidea.tasktracker.model.Task{" +
                "taskId='" + getId() + '\'' +
                ", taskName='" + getName() + '\'' +
                ", taskDescription='" + getDescription() + '\'' +
                ", taskStatus='" + getStatus() + '\'' +
                '}';
    }
}
