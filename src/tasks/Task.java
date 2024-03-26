package tasks;

public class Task {
    private String taskName;
    private String taskDescription;
    private int taskId;
    private TaskStatus taskStatus;

    public Task(int taskId, String taskName, String taskDescription, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getTaskStatus() {
        taskStatus = TaskStatus.NEW;
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "taskId='" + getTaskId() + '\'' +
                ", taskName='" + getTaskName() + '\'' +
                ", taskDescription='" + getTaskDescription() + '\'' +
                ", taskStatus='" + getTaskStatus() + '\'' +
                '}';
    }
}
