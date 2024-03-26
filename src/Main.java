import tasks.Task;
import tasks.TaskManager;
import tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        System.out.println("Поехали!");

        taskManager.createTask("Уроки JAVA", "Сдать проект номер 4", TaskStatus.NEW);
        taskManager.createEpic("Выучить ООП", "Изучить наследование и инкапсуляцию",
                TaskStatus.NEW);
        taskManager.createSubtask("Наследование", "Изучить наследование",
                TaskStatus.IN_PROGRESS);
        taskManager.createSubtask("Инкапсуляция", "Изучить инкапсуляцию",
                TaskStatus.IN_PROGRESS);
        // Получение списка задач
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());
        // Обновление статусов задач
        System.out.println(taskManager.updateTask(new Task(1, "Уроки JAVA",
                "Сдать проект номер 4", TaskStatus.IN_PROGRESS)));
        // Удаление задач
        taskManager.deleteTask(1);
        taskManager.deleteTask(2);
    }
}
