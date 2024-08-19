package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.smartidea.tasktracker.model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(EpicHandler.class.getName());

    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Началась обработка /Epic запроса от клиента.");

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (!body.isEmpty()) {
                    Epic postEpic = gson.fromJson(body, Epic.class);
                    if (postEpic.getId() == 0) {
                        try {
                            manager.createEpic(postEpic);
                            writeResponse(httpExchange, "Эпик создан.", 201);
                        } catch (IntersectionException e) {
                            sendHasInteractions(httpExchange);
                        } catch (Exception e) {
                            sendServerError(httpExchange);
                        }
                    } else {
                        try {
                            manager.updateEpic(postEpic);
                            writeResponse(httpExchange, "Эпик обновлен.", 201);
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
                        List<Epic> epics = manager.getAllEpic();
                        String response = gson.toJson(epics);
                        sendText(httpExchange, response);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        if (manager.getEpicId(id) != null) {
                            Epic epic = manager.getEpicId(id);
                            String response = gson.toJson(epic);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "Epic с id " + id + " отсутствует.");
                        }
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
                break;
            case "DELETE":
                Integer deleteId = getIdFromPath(httpExchange.getRequestURI().getPath());
                try {
                    if (manager.getEpicId(deleteId) != null) {
                        manager.deleteEpic(deleteId);
                        writeResponse(httpExchange, "Epic с id " + deleteId + "- удален.", 200);
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
