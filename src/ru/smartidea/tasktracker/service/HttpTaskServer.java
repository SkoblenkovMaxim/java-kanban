package ru.smartidea.tasktracker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class HttpTaskServer {
    public static final int PORT = 8081;
    private final HttpServer server;
    public TaskManager manager;
    private static final Logger logger = Logger.getLogger(EpicHandler.class.getName());

    public HttpTaskServer(TaskManager manager) {
        try {
            this.manager = manager;
            Gson gson = getGson();
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TaskHandler(manager, gson));
            server.createContext("/subtasks", new SubtaskHandler(manager, gson));
            server.createContext("/epics", new EpicHandler(manager, gson));
            server.createContext("/history", new HistoryHandler(manager, gson));
            server.createContext("/prioritized", new PrioritizedHandler(manager, gson));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HttpTaskServer taskServer = new HttpTaskServer(new InMemoryTaskManager(Managers.getDefaultHistory()));
        taskServer.start();
    }

    public void start() {
        logger.info("HTTP-сервер запущен на " + PORT + " порту!");
        server.start();
    }

    public void stop() {
        logger.info("HTTP-сервер на " + PORT + " порту был остановлен!");
        server.stop(1);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
}
