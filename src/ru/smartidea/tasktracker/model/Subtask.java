package ru.smartidea.tasktracker.model;

import ru.smartidea.tasktracker.service.TaskStatus;
import ru.smartidea.tasktracker.service.Type;

public class Subtask extends Task {
    protected int epicId;
    private Type type;

    public Subtask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }
//id,type,name,status,description,epic
    public Subtask(int id, Type type, String name, TaskStatus status, String description, int epicId) {
        super(id, name, status, description);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(int epicId, String name, String description, int id, TaskStatus status) {
        super(id, name, description, status);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
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

//    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", getId(), getType(), getName(),
                getStatus(), getDescription(), getEpicId());
    }
}
