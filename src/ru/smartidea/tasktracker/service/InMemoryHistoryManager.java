package ru.smartidea.tasktracker.service;

import ru.smartidea.tasktracker.model.Node;
import ru.smartidea.tasktracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> receivedTasksMap;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        receivedTasksMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }

    // Удаление задачи из просмотра
    @Override
    public void remove(int id) {
        removeNode(receivedTasksMap.get(id));
    }

    // Получение последних десяти просмотренных пользователем задач
    @Override
    public List<Task> getHistory() {
        //return new ArrayList<>(getTasks());
        return getTasks();
    }

    public void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.getNext();
            final Node<Task> previous = node.getPrevious();
            node.setData(null);
            receivedTasksMap.remove(node.task.getId());

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

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, tail, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        receivedTasksMap.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new LinkedList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }
}
