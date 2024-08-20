package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.smartidea.tasktracker.model.Task;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(HistoryHandler.class.getName());

    public HistoryHandler(TaskManager manager, Gson gson) {
        super(manager,gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Началась обработка /History запроса от клиента.");
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                List<Task> history = manager.getHistory();
                String response = gson.toJson(history);
                sendText(httpExchange, response);
            } else {
                sendNotFound(httpExchange, "Такого запроса не существует");
            }
        } catch (Exception e) {
            sendServerError(httpExchange);
        }
    }
}
