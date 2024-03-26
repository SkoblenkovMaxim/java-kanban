package tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    ArrayList<Subtask> subtaskArrayList;
    HashMap<Integer, ArrayList<Subtask>> epicWithSubtask;

    public Epic(int taskId, String taskName, String taskDescription, TaskStatus taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
        epicWithSubtask = new HashMap<>();
        epicWithSubtask.put(getTaskId(), subtaskArrayList);
    }

    public void epicStatus(Epic epic) {
        if (epic.subtaskArrayList.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if(epic.subtaskArrayList.contains(TaskStatus.DONE)) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "epicTaskId='" + getTaskId() + '\'' +
                ", epicTaskName='" + getTaskName() + '\'' +
                ", epicTaskDescription='" + getTaskDescription() + '\'' +
                ", epicTaskStatus='" + getTaskStatus() + '\'' +
                '}';
    }
}
