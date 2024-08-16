package service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import ru.smartidea.tasktracker.service.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    Task task = new Task(1,"Task 1", "Description Task 1", TaskStatus.NEW,
            LocalDateTime.now().minusDays(15), Duration.ofMinutes(5));
    Task task2 = new Task(2,"Task 2", "Description Task 2", TaskStatus.NEW,
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
    public void startServers() throws IOException {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    @Test
    void shouldPOSTTask() throws IOException, InterruptedException {
        manager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(url);
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

        assertEquals(task.toString(), manager.getTaskId(task.getId()).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldGETTask() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        Map<Integer, Task> testList = Map.of(task.getId(), task, task2.getId(),task2);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        HashMap<Integer, Task> tasksFromJson =
                gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, Task>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertTrue(tasksFromJson.size() == 2);
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        int id = task.getId();
        manager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int jsonId = jsonObject.get("id").getAsInt();
        String jsonDescription = jsonObject.get("description").getAsString();

        assertEquals(id, jsonId);
        assertTrue(jsonElement.isJsonObject(), "Некорректный JSON");
        assertEquals(task.getDescription(), jsonDescription);
    }

    @Test
    void shouldDELETETask() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldPOSTEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(epic.toString(), manager.getEpicId(epic.getId()).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldGETEpic() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createEpic(epic2);
        Map<Integer, Epic> testList = Map.of(epic.getId(), epic, epic2.getId(), epic2);
        URI url = URI.create("http://localhost:8080/tasks/epic/");
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
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals("Все эпики удалены", response.body());
    }

    @Test
    void shouldPOSTSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(uri);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(subtask.toString(), manager.getSubtaskId(subtask.getId()).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldGETSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        Map<Integer, Subtask> testList = Map.of(subtask.getId(), subtask);
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        HashMap<Integer, Subtask> tasksFromJson =
                gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, Subtask>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertTrue(tasksFromJson.size() == 1);
    }

    @Test
    void shouldDELETESubtask() throws IOException, InterruptedException {
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals("Все подзадачи удалены", response.body());
    }

    @Test
    void shouldGETHistory() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        manager.getTaskId(task.getId());
        manager.getTaskId(task2.getId());
        URI url = URI.create("http://localhost:8080/tasks/history/");
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
        URI url = URI.create("http://localhost:8080/tasks/");
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
