package service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;
import ru.smartidea.tasktracker.service.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.smartidea.tasktracker.service.HttpTaskServer.getGson;

@DisplayName("Проверка класса HttpTaskServer")
public class HttpTaskServerTest {
    private TaskManager manager;
    private HttpTaskServer httpTaskServer;
    private static final Gson gson = getGson();
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    Task task = new Task(1,"Task 1", "Description Task 1", TaskStatus.NEW,
            LocalDateTime.now().minusDays(15), Duration.ofMinutes(5));
    Task task2 = new Task(2,"Task 2", "Description Task 2", TaskStatus.NEW,
            LocalDateTime.now().minusDays(10), Duration.ofMinutes(5));
    Epic epic = new Epic(0,"Epic 1", TaskStatus.NEW, "Description Epic 1",
            LocalDateTime.now().minusDays(9), Duration.ofMinutes(5));
    Epic epic2 = new Epic(4,"Epic 2", TaskStatus.DONE, "Description Epic 2",
            LocalDateTime.now().minusDays(8), Duration.ofMinutes(5));
    Subtask subtask = new Subtask("Subtask 1", "Description Subtask 1", TaskStatus.NEW,
            LocalDateTime.now().minusDays(15), Duration.ofMinutes(5), 0);
    Subtask subtask2 = new Subtask("Subtask 2", "Description Subtask 2", TaskStatus.DONE,
            LocalDateTime.now().minusDays(10), Duration.ofMinutes(5), 0);

    @BeforeEach
    public void startServers() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    @Test
    void shouldPOSTTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8081/tasks");

        String json = gson.toJson(task);

        HttpClient client = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(body)
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getAllTask().size());
    }

    @Test
    void shouldGETTask() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);

        URI url = URI.create("http://localhost:8081/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        List<Task> tasksFromJson = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, tasksFromJson.size());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);

        URI url = URI.create("http://localhost:8081/tasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();

        assertEquals(task.getName(), name);

        URI url1 = URI.create("http://localhost:8081/tasks/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);

        assertEquals(200, response1.statusCode());

        JsonElement jsonElement1 = JsonParser.parseString(response1.body());
        JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
        String name1 = jsonObject1.get("name").getAsString();

        assertEquals(task2.getName(), name1);
    }

    @Test
    void shouldDELETETask() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);

        assertEquals(2, manager.getAllTask().size());

        URI url = URI.create("http://localhost:8081/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllTask().size());
    }

    @Test
    void shouldPOSTEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8081/epics");

        String json = gson.toJson(epic);

        HttpClient client = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getAllEpic().size());
    }

    @Test
    void shouldGETEpic() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createEpic(epic2);

        URI url = URI.create("http://localhost:8081/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        List<Epic> epicsFromJson = gson.fromJson(response.body(), new TypeToken<List<Epic>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, epicsFromJson.size());
    }

    @Test
    void shouldDELETEEpic() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createEpic(epic2);

        assertEquals(2, manager.getAllEpic().size());

        URI url = URI.create("http://localhost:8081/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllEpic().size());
    }

    @Test
    void shouldPOSTSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);

        URI uri = URI.create("http://localhost:8081/subtasks");

        String json = gson.toJson(subtask);

        HttpClient client = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getAllSubtask().size());
    }

    @Test
    void shouldGETSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8081/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        List<Subtask> tasksFromJson = gson.fromJson(response.body(), new TypeToken<List<Subtask>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, tasksFromJson.size());
    }

    @Test
    void shouldDELETESubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8081/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getAllSubtask().size());
    }

    @Test
    void shouldGETHistory() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        manager.getTaskId(task.getId());
        manager.getTaskId(task2.getId());
        URI url = URI.create("http://localhost:8081/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(200, response.statusCode());
        assertTrue(jsonElement.isJsonArray(), "Некорректный JSON");
        assertEquals(2, jsonArray.size());
    }

    @Test
    void shouldGETPrioritized() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        task.setStartTime(LocalDateTime.from(LocalDateTime.now()));
        task2.setStartTime(LocalDateTime.from(LocalDateTime.now().plusMinutes(30)));
        URI url = URI.create("http://localhost:8081/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertEquals(200, response.statusCode());
        assertTrue(jsonElement.isJsonArray(), "Некорректный JSON");
        assertEquals(2, jsonElement.getAsJsonArray().size());
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }
}
