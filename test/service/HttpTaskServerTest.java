package service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import ru.smartidea.tasktracker.service.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.smartidea.tasktracker.model.Epic;
import ru.smartidea.tasktracker.model.Subtask;
import ru.smartidea.tasktracker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Проверка класса HttpTaskServer")
public class HttpTaskServerTest {
    private TaskManager manager;
    private HttpTaskServer httpTaskServer;
    private static final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    Task task = new Task(1,"Task_1", "Description Task 1", TaskStatus.NEW,
            LocalDateTime.now().minusDays(15), Duration.ofMinutes(5));
    Task task2 = new Task(2,"Task_2", "Description Task 2", TaskStatus.NEW,
            LocalDateTime.now().minusDays(10), Duration.ofMinutes(5));
    Epic epic = new Epic(3,"Epic 1", TaskStatus.NEW, "Description Epic 1",
            LocalDateTime.now().minusDays(9), Duration.ofMinutes(5));
    Epic epic2 = new Epic(4,"Epic 2", TaskStatus.DONE, "Description Epic 2",
            LocalDateTime.now().minusDays(8), Duration.ofMinutes(5));
    Subtask subtask = new Subtask("Subtask 1", "Description Subtask 1", TaskStatus.NEW,
            LocalDateTime.now().minusDays(15), Duration.ofMinutes(5), 3);
    Subtask subtask2 = new Subtask("Subtask 2", "Description Subtask 2", TaskStatus.DONE,
            LocalDateTime.now().minusDays(10), Duration.ofMinutes(5), 4);

    @BeforeEach
    public void startServers() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    @Test
    void shouldPOSTTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8081/tasks");

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

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
        JsonElement jsonElement = JsonParser.parseString(response.body());

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        List<Task> tasksFromJson = gson.fromJson(jsonElement, new TypeToken<List<Task>>(){}.getType());

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

        assertEquals(1, manager.getAllTask().size());
    }

    @Test
    void shouldPOSTEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8081/epics");

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        HashMap<Integer, Task> tasksFromJson =
                gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, Epic>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertTrue(tasksFromJson.size() == 2);
    }

    @Test
    void shouldDELETEEpic() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createEpic(epic2);
        URI url = URI.create("http://localhost:8081/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(404, response.statusCode());
    }

    @Test
    void shouldPOSTSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        URI uri = URI.create("http://localhost:8081/subtasks");
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
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
        assertEquals(1, manager.getAllSubtask().size());
    }

    @Test
    void shouldGETSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        Map<Integer, Subtask> testList = Map.of(subtask.getId(), subtask);
        URI url = URI.create("http://localhost:8081/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        HashMap<Integer, Subtask> tasksFromJson =
                gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, Subtask>>(){}.getType());

        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDELETESubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        //assertEquals(2, manager.getAllSubtask().size());
        URI url = URI.create("http://localhost:8081/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(404, response.statusCode());
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
