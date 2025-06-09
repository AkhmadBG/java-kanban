package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

import static java.net.HttpURLConnection.*;
import static utils.AppConstants.DEFAULT_CHARSET;

public class BaseHttpHandler {

    protected void sendResponse(HttpExchange exchange,
                                String responseString,
                                int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String responseString) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(HTTP_NOT_FOUND, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(HTTP_NOT_ACCEPTABLE, 0);
            os.write("Пересечение задач, выберите другое время".getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

}