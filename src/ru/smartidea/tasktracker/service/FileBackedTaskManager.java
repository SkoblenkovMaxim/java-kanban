package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        this.file = new File("resources/", "task.csv");
    }

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    // Метод сохранения задач в файл
    public void save() {
        try {
            if (!Files.exists(file.toPath())) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Отсутствует файл для записи данных");
        }

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTask()) {
                writer.write(task.toStringFromFile(task) + "\n");
            }

            for (Epic epic : getAllEpic()) {
                writer.write(epic.toStringFromFile(epic) + "\n");
            }

            for (Subtask subtask : getAllSubtask().values()) {
                writer.write(subtask.toStringFromFile() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл" + e);
        }
    }

    // Метод создания задачи из строки
    private static Task fromString(String value) {
        String[] element = value.split(",");
        int id = Integer.parseInt(element[0]);
        String type = element[1];
        String name = element[2];
        TaskStatus status = TaskStatus.valueOf(element[3].toUpperCase());
        String description = element[4];
        int epicId = 0;

        if (element.length == 6) {
            epicId = Integer.parseInt(element[5]);
        }
        if (type.equals("Epic")) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else if (type.equals("Subtask")) {
            Subtask subtask = new Subtask(name, description, status, epicId);
            subtask.setId(id);
            return subtask;
        } else {
            Task task = new Task(name, description, status);
            task.setId(id);
            return task;
        }
    }

    // Метод восстановления данных менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (line.contains("id")) {
                    continue;
                }

                Task task = fromString(line);

                if (task instanceof Epic epic) {
                    manager.createEpic(epic);
                } else if (task instanceof Subtask subtask) {
                    manager.createSubtask(subtask);
                } else {
                    manager.createTask(task);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка чтения данных из файла!");
        }
        return manager;
    }

    // Задачи
    // Создание задач
    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    // Получение по идентификатору задачи
    @Override
    public Task getTaskId(Integer newTaskId) {
        Task task = super.getTaskId(newTaskId);
        if (task != null) {
            save();
            return task;
        } else {
            return null;
        }
    }

    // Удаление задачи по id
    @Override
    public void deleteTask(int idTask) {
        super.deleteTask(idTask);
        save();
    }

    // Удаление всех задач
    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    // Обновление задач
    @Override
    public Map<Integer, Task> updateTask(Task task) {
        super.updateTask(task);
        save();
        return getTaskOrdinaryMap();
    }

    // Эпики
    // Создание эпика
    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    // Получение по идентификатору эпик задачи
    @Override
    public Epic getEpicId(Integer newTaskId) {
        Epic epic = super.getEpicId(newTaskId);
        if (epic != null) {
            save();
            return epic;
        } else {
            return null;
        }

    }

    // Удаление эпика
    @Override
    public void deleteEpic(int idTask) {
        super.deleteEpic(idTask);
        save();
    }

    // Удаление всех эпик задач
    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    // Обновление эпика
    @Override
    public Map<Integer, Epic> updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return getTaskEpicMap();
    }

    // Обновление статуса эпика
    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    // Подзадачи
    // Создание подзадач
    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    // Удаление всех подзадач
    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    // Получение по идентификатору подзадачи
    @Override
    public Subtask getSubtaskId(Integer newTaskId) {
        Subtask subtask = super.getSubtaskId(newTaskId);
        save();
        return  subtask;
    }

    // Обновление подзадач
    @Override
    public Map<Integer, Subtask> updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return getTaskSubMap();
    }

    // Удаление подзадач по ID
    @Override
    public void deleteSubtask(int id, Epic epic) {
        super.deleteSubtask(id, epic);
        save();
    }
}
