package http;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import model.Epic;
import utils.DurationAdapter;
import utils.EpicDeserializer;
import utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.net.HttpURLConnection.*;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                handleGetRequest(exchange, requestPath);
                break;
            case "POST":
                handlePostRequest(exchange);
                break;
            case "DELETE":
                handleDeleteRequest(exchange, requestPath);
                break;
            default:
                sendResponse(exchange, "Метод не поддерживается", HTTP_BAD_METHOD);
        }

    }

    private void handleGetRequest(HttpExchange exchange, String requestPath) throws IOException {
        String[] pathParts = requestPath.split("/");
        String response;
        if (pathParts.length > 3 && pathParts[3].equals("subtasks")) {
            int epicId = Integer.parseInt(pathParts[2]);
            if (taskManager.getEpic(epicId) == null) {
                sendNotFound(exchange, "Эпик с id " + epicId + " не найдена");
                return;
            }
            if (taskManager.getAllSubTasksInEpic(epicId).isEmpty()) {
                sendNotFound(exchange, "У эпика с id " + epicId + " нет подзадач");
                return;
            }
            response = gson.toJson(taskManager.getAllSubTasksInEpic(epicId));
        } else if (pathParts.length > 2) {
            int epicId = Integer.parseInt(pathParts[2]);
            if (taskManager.getEpic(epicId) == null) {
                sendNotFound(exchange, "Эпик с id " + epicId + " не найдена");
                return;
            }
            response = gson.toJson(taskManager.getEpic(epicId));
        } else {
            if (taskManager.getEpics().isEmpty()) {
                sendNotFound(exchange, "Список эпиков пуст");
                return;
            }
            response = gson.toJson(taskManager.getEpics());
        }
        sendResponse(exchange, response, HTTP_OK);
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            System.out.println("Ответ от сервера не соответствует ожидаемому.");
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic epic = gson.fromJson(jsonObject, Epic.class);
        if (jsonObject.has("id")) {
            int epicId = jsonObject.get("id").getAsInt();
            epic.setId(epicId);
            taskManager.updateEpic(epic);
        } else {
            if (taskManager.checkIntersection(epic)) {
                sendHasInteractions(exchange);
                return;
            }
            taskManager.addNewEpic(epic);
        }
        String response = gson.toJson(epic);
        sendResponse(exchange, response, HTTP_OK);
    }

    private void handleDeleteRequest(HttpExchange exchange, String requestPath) throws IOException {
        String[] pathParts = requestPath.split("/");
        String response;
        if (pathParts.length > 2) {
            int epicId = Integer.parseInt(pathParts[2]);
            if (taskManager.getEpic(epicId) == null) {
                sendNotFound(exchange, "Задача с id " + epicId + " не найдена");
                return;
            }
            taskManager.deleteEpic(epicId);
            response = "Задача с id " + epicId + " успешно удалена";
            sendResponse(exchange, response, HTTP_OK);
        } else {
            sendNotFound(exchange, "Укажите id задачи для удаления");
        }
    }
}
