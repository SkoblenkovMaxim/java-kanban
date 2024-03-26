package tasks;

public class Subtask extends Task {
    Epic epic;
    public Subtask(int taskId, String taskName, String taskDescription, TaskStatus taskStatus, Epic epic) {
        super(taskId, taskName, taskDescription, taskStatus);
        this.epic = epic;
    }
    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "subtaskId='" + getTaskId() + '\'' +
                ", subtaskName='" + getTaskName() + '\'' +
                ", subtaskDescription='" + getTaskDescription() + '\'' +
                ", subtaskStatus='" + getTaskStatus() + '\'' +
                '}';
    }
}
