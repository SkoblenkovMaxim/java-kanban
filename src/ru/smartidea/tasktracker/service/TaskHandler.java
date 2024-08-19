package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.smartidea.tasktracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(TaskHandler.class.getName());

    public TaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Началась обработка /Task запроса от клиента.");

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (!body.isEmpty()) {
                    Task postTask = gson.fromJson(body, Task.class);
                    if (postTask.getId() == 0) {
                        try {
                            manager.createTask(postTask);
                            writeResponse(httpExchange, "Задача создана.", 201);
                        } catch (IntersectionException e) {
                            sendHasInteractions(httpExchange);
                        } catch (Exception e) {
                            sendServerError(httpExchange);
                        }
                    } else {
                        try {
                            manager.updateTask(postTask);
                            writeResponse(httpExchange, "Задача обновлена.", 201);
                        } catch (IntersectionException e) {
                            sendHasInteractions(httpExchange);
                        } catch (Exception e) {
                            sendServerError(httpExchange);
                        }
                    }
                } else {
                    throw new RuntimeException("Данные не переданы");
                }
            case "GET":
                Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (id == null) {
                    try {
                        List<Task> tasks = manager.getAllTask();
                        String response = gson.toJson(tasks);
                        sendText(httpExchange, response);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        if (manager.getTaskId(id) != null) {
                            Task task = manager.getTaskId(id);
                            String response = gson.toJson(task);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "Задача с id " + id + " отсутствует.");
                        }
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
            case "DELETE":
                Integer deleteId = getIdFromPath(httpExchange.getRequestURI().getPath());
                try {
                    if (manager.getTaskId(deleteId) != null) {
                        manager.deleteTask(deleteId);
                        writeResponse(httpExchange, "Задача с id " + deleteId + "- удален.", 200);
                    } else {
                        sendNotFound(httpExchange,
                                "Задача с id " + deleteId + " отсутствует. Уточните id задачи и повторите запрос");
                    }
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
            default:
                try {
                    sendNotFound(httpExchange, "Такого запроса не существует");
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
        }
    }
}
