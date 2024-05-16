package ru.smartidea.tasktracker;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.*;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("TaskName1", "TaskDescription1", TaskStatus.NEW);
        Epic epic1 = new Epic("EpicName1", "EpicDescription1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("SubtaskName1", "SubtaskDescription1", TaskStatus.NEW, 2);
        Subtask subtask2 = new Subtask("SubtaskName2", "SubtaskDescription2", TaskStatus.NEW, 2);

        TaskManager taskManager = Managers.getDefault();
        System.out.println("Поехали!");
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.getTaskId(1);
        taskManager.getEpicId(2);
        taskManager.getSubtaskId(3);
        // Получение списка задач
        printAllTasks(taskManager);
        // Обновление статусов задач
        System.out.println(taskManager.updateTask(new Task(1, "TaskName1",
                "TaskDescription1", TaskStatus.IN_PROGRESS)));
        // Удаление задач
        taskManager.deleteTask(1);
        taskManager.deleteTask(2);
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTask()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpic()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubtask().values()) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtask().values()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        Managers.getDefault();
    }
}
