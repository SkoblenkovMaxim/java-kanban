package tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    static int taskId = 0; // Идентификатор задачи
    Epic epic;
    HashMap<Integer, Task> taskOrdinaryMap = new HashMap<>();
    HashMap<Integer, Epic> taskEpicMap = new HashMap<>();
    HashMap<Integer, Subtask> taskSubMap = new HashMap<>();

    // Получение списка задач
    public ArrayList<Task> getAllTask() {
        for (Integer t : taskOrdinaryMap.keySet()) {
            taskOrdinaryMap.get(t);
        }
        return new ArrayList<>(taskOrdinaryMap.values());
    }
    // Получение всех эпик задач
    public ArrayList<Epic> getAllEpic() {
        for (Integer t : taskEpicMap.keySet()) {
            taskEpicMap.get(t);
        }
        return new ArrayList<>(taskEpicMap.values());
    }
    // Получение всех подзадач
    public ArrayList<Subtask> getAllSubtask() {
        for (Integer t : taskSubMap.keySet()) {
            taskSubMap.get(t);
        }
        return new ArrayList<>(taskSubMap.values());
    }
    // Удаление всех задач
    public void removeAllTask() {
        taskOrdinaryMap.clear();
    }
    // Удаление всех эпик задач
    public void removeAllEpic() {
        taskEpicMap.clear();
    }
    // Удаление всех подзадач
    public void removeAllSubtask() {
        taskSubMap.clear();
    }
    // Получение по идентификатору задачи
    public void getTaskId(Integer newTaskId) {
        taskOrdinaryMap.get(newTaskId);
    }
    // Получение по идентификатору эпик задачи
    public void getEpicId(Integer newTaskId) {
        taskEpicMap.get(newTaskId);
    }
    // Получение по идентификатору подзадачи
    public void getSubtask(Integer newTaskId) {
        taskSubMap.get(newTaskId);
    }
    // Создание задачи
    public void createTask(String taskName, String taskDescription, TaskStatus taskStatus) {
        taskId++;
        int taskId = TaskManager.taskId;
        Task task = new Task(taskId, taskName, taskDescription, taskStatus);
        taskOrdinaryMap.put(taskId, task);
        //System.out.println(taskOrdinaryMap);
    }
    // Создание эпик
    public void createEpic(String taskName, String taskDescription, TaskStatus taskStatus) {
        taskId++;
        int taskId = TaskManager.taskId;
        epic = new Epic(taskId, taskName, taskDescription, taskStatus);
        taskEpicMap.put(taskId, epic);
    }
    // Создание подзадачи
    public void createSubtask(String taskName, String taskDescription, TaskStatus taskStatus) {
        taskId++;
        int taskId = TaskManager.taskId;
        Subtask subtask = new Subtask(taskId, taskName, taskDescription, taskStatus, epic);
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        taskSubMap.put(taskId, subtask);
    }
    // Обновление Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public Collection<Task> updateTask(Task task) {
        taskOrdinaryMap.put(taskId, task);
        return taskOrdinaryMap.values();
    }

    public Collection<Epic> updateEpic(Epic epic) {
        taskEpicMap.put(epic.getTaskId(), epic);
        return taskEpicMap.values();
    }

    public Collection<Subtask> updateSubtask(Subtask subtask) {
        taskSubMap.put(subtask.getTaskId(), subtask);
        epic.epicStatus(epic);
        return taskSubMap.values();
    }
    // Удаление
    public void deleteTask(int idTask) {
        taskId = idTask;
        taskOrdinaryMap.remove(idTask);
    }

    public void deleteEpic(int idTask) {
        taskId = idTask;
        taskEpicMap.remove(idTask);
    }

    public void deleteSubtask(int idTask) {
        taskId = idTask;
        taskSubMap.remove(idTask);
    }
}
