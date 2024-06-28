package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;
import ru.smartidea.tasktracker.service.Type;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task() {

    }

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

    public Task(int id, String name, TaskStatus status, String description) {
        this.id = id;
        getType();
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public Type getType() {
        return Type.TASK;
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

    // Метод сохранения задачи в строку
    public String toStringFromFile(Task task) {
        return String.format("%s,%s,%s,%s,%s,%s", task.getId(), task.getType(), task.getName(), task.getStatus(),
                task.getDescription(), "");
    }
}
