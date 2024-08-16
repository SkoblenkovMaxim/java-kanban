package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /Epic запроса от клиента.");

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (!body.isEmpty()) {
                    Epic postTask = gson.fromJson(body, Epic.class);
                    try {
                        manager.createEpic(postTask);
                        writeResponse(httpExchange, "Задача создана.", 201);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    throw new RuntimeException("Данные не переданы");
                }
            case "GET":
                Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
                String subPath = getPath(httpExchange.getRequestURI().getPath());
                if (manager.getEpicId(id) != null) {
                    if (id == null) {
                        List<Epic> tasks = manager.getAllEpic();
                        String response = gson.toJson(tasks);
                        sendText(httpExchange, response);
                    } else if (subPath != null) {
                        List<Integer> epicsSubTasks = manager.getEpicId(id).getSubtaskIds();
                        String response = gson.toJson(epicsSubTasks);
                        sendText(httpExchange, response);
                    } else {
                        if (manager.getEpicId(id) != null) {
                            Task task = manager.getEpicId(id);
                            String response = gson.toJson(task);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "Epic с id " + id + " отсутствует.");
                        }
                    }
                } else {
                    sendNotFound(httpExchange, "Эпика с id " + id + " нет.");
                }
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
            default:
                try {
                    sendNotFound(httpExchange, "Такого запроса не существует");
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
        }
    }
}
