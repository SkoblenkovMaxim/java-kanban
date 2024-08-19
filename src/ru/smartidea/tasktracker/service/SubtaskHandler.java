package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.smartidea.tasktracker.model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.logging.Logger;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(SubtaskHandler.class.getName());

    public SubtaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Началась обработка /SubTask запроса от клиента.");

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (!body.isEmpty()) {
                    Subtask postSubtask = gson.fromJson(body, Subtask.class);
                    if (postSubtask.getId() == 0) {
                        try {
                            manager.createSubtask(postSubtask);
                            writeResponse(httpExchange, "Задача создана.", 201);
                        } catch (IntersectionException e) {
                            sendHasInteractions(httpExchange);
                        } catch (Exception e) {
                            sendServerError(httpExchange);
                        }
                    } else {
                        try {
                            manager.updateSubtask(postSubtask);
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
                break;
            case "GET":
                Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (id == null) {
                    try {
                        Collection<Subtask> tasks = manager.getAllSubtask().values();
                        String response = gson.toJson(tasks);
                        sendText(httpExchange, response);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        if (manager.getSubtaskId(id) != null) {
                            Subtask subtask = manager.getSubtaskId(id);
                            String response = gson.toJson(subtask);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "SubTask с id " + id + " отсутствует.");
                        }
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
                break;
            case "DELETE":
                Integer deleteId = getIdFromPath(httpExchange.getRequestURI().getPath());
                try {
                    if (manager.getSubtaskId(deleteId) != null) {
                        manager.deleteTask(deleteId);
                        writeResponse(httpExchange, "SubTask с id " + deleteId + "- удален.", 200);
                    } else {
                        sendNotFound(httpExchange, "SubTask с id " + deleteId + " отсутствует. Уточните id задачи и повторите запрос");
                    }
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
                break;
            default:
                try {
                    sendNotFound(httpExchange, "Такого запроса не существует");
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
                break;
        }
    }
}
