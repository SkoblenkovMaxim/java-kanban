package ru.smartidea.tasktracker;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        File file = new File("resources/task.csv");
        Task task1 = new Task("TaskName1", "TaskDescription1", TaskStatus.NEW);
        Epic epic1 = new Epic("EpicName1", "EpicDescription1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("SubtaskName1", "SubtaskDescription1", TaskStatus.NEW, 2);
        Subtask subtask2 = new Subtask("SubtaskName2", "SubtaskDescription2", TaskStatus.NEW, 2);

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(historyManager);
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubtask(subtask1);
        fileBackedTaskManager.createSubtask(subtask2);

        FileBackedTaskManager.loadFromFile(file);
    }
}
