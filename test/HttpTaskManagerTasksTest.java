import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.TaskStatus;
import http.HttpTaskServer;
import managers.TaskManager;
import managers.impl.InMemoryTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

import static java.net.HttpURLConnection.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpTaskManagerTasksTest {

    TaskManager manager;
    HttpTaskServer taskServer;
    Gson gson;
    HttpClient client;

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
        gson = taskServer.getGson();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    void shouldReturnEmptyTaskList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, response.statusCode());
        assertEquals("Список задач пуст", response.body());
    }

    @Test
    void shouldReturnEmptyEpicList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, response.statusCode());
        assertEquals("Список эпиков пуст", response.body());
    }

    @Test
    void shouldReturnEmptySubTaskList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, response.statusCode());
        assertEquals("Список подзадач пуст", response.body());
    }

    @Test
    void shouldCreateAndRetrieveTask() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание");
        task.setTaskStatus(TaskStatus.NEW);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, postResponse.statusCode());
        assertTrue(postResponse.body().contains("Задача"));

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, getResponse.statusCode());
    }

    @Test
    void shouldCreateAndRetrieveEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        epic.setTaskStatus(TaskStatus.NEW);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, postResponse.statusCode());
        assertTrue(postResponse.body().contains("Эпик"));

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, getResponse.statusCode());
    }

    @Test
    void shouldCreateAndRetrieveSubTask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Подзадача", "Описание", 1);
        subTask.setTaskStatus(TaskStatus.NEW);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, postResponse.statusCode());
        assertTrue(postResponse.body().contains("Подзадача"));

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, getResponse.statusCode());
    }

    @Test
    void shouldReturn404ForUnknownEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/unknown"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание");
        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, response.statusCode());

        Task returnedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getName(), returnedTask.getName());
        assertEquals(task.getDescription(), returnedTask.getDescription());
    }

    @Test
    void shouldCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        String json = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, response.statusCode());

        Task returnedTask = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic.getName(), returnedTask.getName());
        assertEquals(epic.getDescription(), returnedTask.getDescription());
    }


    @Test
    void shouldCreateSubTask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Задача", "Описание", 1);
        String json = gson.toJson(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, response.statusCode());

        Task returnedTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask.getName(), returnedTask.getName());
        assertEquals(subTask.getDescription(), returnedTask.getDescription());
    }

    @Test
    void shouldGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание");
        manager.addNewTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, response.statusCode());
        Type taskMapType = new TypeToken<Map<String, Task>>() {
        }.getType();
        Map<String, Task> taskMap = gson.fromJson(response.body(), taskMapType);
        assertFalse(taskMap.isEmpty());
    }

    @Test
    void shouldReturnNotFound() throws IOException, InterruptedException {
        for (Task task : manager.getAllTasks()) {
            manager.deleteTask(task.getId());
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, response.statusCode());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание");
        manager.addNewTask(task);
        int taskId = task.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, response.statusCode());
        assertNull(manager.getTask(taskId));
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        manager.addNewEpic(epic);
        int epicId = epic.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epicId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, response.statusCode());
        assertNull(manager.getEpic(epicId));
    }

    @Test
    void shouldDeleteSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        SubTask subTask = new SubTask("Подзадача", "Описание", 1);
        manager.addNewEpic(epic);
        manager.addNewSubTask(subTask);
        int subTaskId = subTask.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subTaskId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_OK, response.statusCode());
        assertNull(manager.getSubTask(subTaskId));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentTask() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/99999"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(HTTP_NOT_FOUND, response.statusCode());
    }

}
