package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            handleGetRequest(exchange);
        } else {
            sendResponse(exchange, "Метод не поддерживается", 405);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        Set<Task> prioritisedTasks = taskManager.getPrioritisedTasks();
        String response;
        //Gson gson = GsonConfig.buildGson();
        if (prioritisedTasks.isEmpty()) {
            response = "Список приоритетных задач пуст";
            sendNotFound(exchange, response);
        } else {
            response = gson.toJson(prioritisedTasks);
            sendResponse(exchange, response, 200);
        }
    }

}
