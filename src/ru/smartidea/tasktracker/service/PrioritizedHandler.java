package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.smartidea.tasktracker.model.Task;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(PrioritizedHandler.class.getName());

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager,gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("Началась обработка /Prioritized запроса от клиента.");
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                List<Task> prioritized = manager.getPrioritizedTasks();
                String response = gson.toJson(prioritized);
                sendText(httpExchange, response);
            } else {
                writeResponse(httpExchange, "Такого запроса не существует", 404);
            }
        } catch (Exception e) {
            sendServerError(httpExchange);
        }
    }
}
