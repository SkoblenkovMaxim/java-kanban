package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Node;
import ru.smartidea.tasktracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> receivedTasksMap;
    private Node<Task> head;
    private Node<Task> tail;

    private List<Task> historyTask = new ArrayList<>();

    public List<Task> getHistoryTask() {
        return getTasks();
    }

    public void setHistoryTask(List<Task> historyTask) {
        this.historyTask = historyTask;
    }

    public InMemoryHistoryManager() {
        receivedTasksMap = new HashMap<>();
    }

    // Добавление просмотренных задач
    @Override
    public Task addTask(Task task) {
        if (task != null) {
            linkLast(task);
        }
        return task;
    }

    // Удаление задачи из просмотра
    @Override
    public void remove(int id) {
        removeNode(receivedTasksMap.get(id));
    }

    // Получение последних десяти просмотренных пользователем задач
    @Override
    public List<Task> getHistory() {
        return getHistoryTask();
    }

    public void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.getNext();
            final Node<Task> previous = node.getPrevious();
            node.setData(null);

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail != node) {
                head = next;
                head.setPrevious(null);
            } else if (head != node && tail == node) {
                tail = previous;
                tail.setNext(null);
            } else {
                previous.setNext(next);
                next.setPrevious(previous);
            }
        }
    }

    private void linkLast(Task task) {
        if (receivedTasksMap.containsKey(task.getId())) {
            removeNode(receivedTasksMap.get(task.getId()));
        }
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, tail, null);
        tail = newNode;
        receivedTasksMap.put(task.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new LinkedList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }
}
