package http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager, Gson gson) {
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
//        Gson gson = GsonConfig.buildGson();
        String response;
        if (pathParts.length > 2) {
            int taskId = Integer.parseInt(pathParts[2]);
            if (taskManager.getTask(taskId) == null) {
                sendNotFound(exchange, "Задача с id " + taskId + " не найдена");
            }
            response = gson.toJson(taskManager.getTask(taskId));
        } else {
            if (taskManager.getTasks().isEmpty()) {
                sendNotFound(exchange, "Список задач пуст");
            }
            response = gson.toJson(taskManager.getTasks());
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
        String taskName = jsonObject.get("name").getAsString();
        String taskDescription = jsonObject.get("description").getAsString();
        Task task = new Task(taskName, taskDescription);
        if (taskManager.checkIntersection(task)) {
            sendHasInteractions(exchange);
        }
        if (jsonObject.has("id")) {
            int taskId = jsonObject.get("id").getAsInt();
            task.setId(taskId);
            taskManager.updateTask(task);
        } else {
            taskManager.addNewTask(task);
        }
        String response = gson.toJson(task);
        sendResponse(exchange, response, 200);
    }

    private void handleDeleteRequest(HttpExchange exchange, String requestPath) throws IOException {
        String[] pathParts = requestPath.split("/");
        String response;
        if (pathParts.length > 2) {
            int taskId = Integer.parseInt(pathParts[2]);
            if (taskManager.getTask(taskId) == null) {
                sendNotFound(exchange, "Задача с id " + taskId + " не найдена");
            }
            taskManager.deleteTask(taskId);
            response = "Задача с id " + taskId + " успешно удалена";
            sendResponse(exchange, response, 200);
        } else {
            sendNotFound(exchange, "Укажите id задачи для удаления");
        }
    }
}
