package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.smartidea.tasktracker.model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager,gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /Prioritized запроса от клиента.");
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
