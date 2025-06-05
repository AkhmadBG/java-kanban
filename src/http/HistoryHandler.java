package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
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
        List<Task> historyTasks = taskManager.getHistory();
        String response;
        //Gson gson = GsonConfig.buildGson();
        if (historyTasks.isEmpty()) {
            response = "В истории нет задач";
            sendNotFound(exchange, response);
        } else {
            response = gson.toJson(historyTasks);
            sendResponse(exchange, response, 200);
        }
    }

}
