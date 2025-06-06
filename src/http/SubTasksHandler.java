package http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import model.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static java.net.HttpURLConnection.*;

public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public SubTasksHandler(TaskManager taskManager, Gson gson) {
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
                sendResponse(exchange, "Метод не поддерживается", HTTP_BAD_METHOD);
        }

    }

    private void handleGetRequest(HttpExchange exchange, String requestPath) throws IOException {
        String[] pathParts = requestPath.split("/");
        String response;
        if (pathParts.length > 2) {
            int subTaskId = Integer.parseInt(pathParts[2]);
            if (taskManager.getSubTask(subTaskId) == null) {
                sendNotFound(exchange, "Подзадача с id " + subTaskId + " не найдена");
            }
            response = gson.toJson(taskManager.getSubTask(subTaskId));
        } else {
            if (taskManager.getSubTasks().isEmpty()) {
                sendNotFound(exchange, "Список подзадач пуст");
            }
            response = gson.toJson(taskManager.getSubTasks());
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
        SubTask subTask = gson.fromJson(jsonObject, SubTask.class);
        if (jsonObject.has("id")) {
            int subTaskId = jsonObject.get("id").getAsInt();
            subTask.setId(subTaskId);
            taskManager.updateSubTask(subTask);
        } else {
            if (taskManager.checkIntersection(subTask)) {
                sendHasInteractions(exchange);
            }
            taskManager.addNewSubTask(subTask);
        }
        String response = gson.toJson(subTask);
        sendResponse(exchange, response, HTTP_OK);
    }

    private void handleDeleteRequest(HttpExchange exchange, String requestPath) throws IOException {
        String[] pathParts = requestPath.split("/");
        String response;
        if (pathParts.length > 2) {
            int subTaskId = Integer.parseInt(pathParts[2]);
            if (taskManager.getSubTask(subTaskId) == null) {
                sendNotFound(exchange, "Подзадача с id " + subTaskId + " не найдена");
            }
            taskManager.deleteSubTask(subTaskId);
            response = "Подзадача с id " + subTaskId + " успешно удалена";
            sendResponse(exchange, response, HTTP_OK);
        } else {
            sendNotFound(exchange, "Укажите id подзадачи для удаления");
        }
    }
}
