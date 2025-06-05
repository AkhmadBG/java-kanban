package http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
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
                sendResponse(exchange, "Метод не поддерживается", 405);
        }

    }

    private void handleGetRequest(HttpExchange exchange, String requestPath) throws IOException {
        String[] pathParts = requestPath.split("/");
        //Gson gson = GsonConfig.buildGson();
        String response;
        if (pathParts.length > 3 && pathParts[3].equals("subtasks")) {
            int epicId = Integer.parseInt(pathParts[2]);
            if (taskManager.getEpic(epicId) == null) {
                sendNotFound(exchange, "Эпик с id " + epicId + " не найдена");
            }
            if (taskManager.getAllSubTasksInEpic(epicId).isEmpty()) {
                sendNotFound(exchange, "У эпика с id " + epicId + " нет подзадач");
            }
            response = gson.toJson(taskManager.getAllSubTasksInEpic(epicId));
        } else if (pathParts.length > 2) {
            int epicId = Integer.parseInt(pathParts[2]);
            if (taskManager.getEpic(epicId) == null) {
                sendNotFound(exchange, "Эпик с id " + epicId + " не найдена");
            }
            response = gson.toJson(taskManager.getEpic(epicId));
        } else {
            if (taskManager.getEpics().isEmpty()) {
                sendNotFound(exchange, "Список эпиков пуст");
            }
            response = gson.toJson(taskManager.getEpics());
        }
        sendResponse(exchange, response, 200);
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        //Gson gson = GsonConfig.buildGson();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            System.out.println("Ответ от сервера не соответствует ожидаемому.");
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String epicName = jsonObject.get("name").getAsString();
        String epicDescription = jsonObject.get("description").getAsString();
        Epic epic = new Epic(epicName, epicDescription);
        if (taskManager.checkIntersection(epic)) {
            sendHasInteractions(exchange);
        }
        if (jsonObject.has("id")) {
            int epicId = jsonObject.get("id").getAsInt();
            epic.setId(epicId);
            taskManager.updateEpic(epic);
        } else {
            taskManager.addNewEpic(epic);
        }
        String response = gson.toJson(epic);
        sendResponse(exchange, response, 200);
    }

    private void handleDeleteRequest(HttpExchange exchange, String requestPath) throws IOException {
        String[] pathParts = requestPath.split("/");
        String response;
        if (pathParts.length > 2) {
            int epicId = Integer.parseInt(pathParts[2]);
            if (taskManager.getEpic(epicId) == null) {
                sendNotFound(exchange, "Задача с id " + epicId + " не найдена");
            }
            taskManager.deleteEpic(epicId);
            response = "Задача с id " + epicId + " успешно удалена";
            sendResponse(exchange, response, 200);
        } else {
            sendNotFound(exchange, "Укажите id задачи для удаления");
        }
    }
}
