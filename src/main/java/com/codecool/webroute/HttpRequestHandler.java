package com.codecool.webroute;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class HttpRequestHandler {
    final Map<String, Method> handlerMethods;

    public HttpRequestHandler(Map<String, Method> handlerMethods) {
        this.handlerMethods = handlerMethods;
    }

    /**
     * Invokes proper method handling proper endpoint and sends HTTP Response back.
     * @param httpExchange Encapsulated HTTP request.
     */
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().toString();
        String message = "";
        if(!handlerMethods.containsKey(path)) {
            sendResponse(httpExchange, 404, "No proper method was found");
        }
        try {
            Object obj = handlerMethods.get(path).invoke(null);
            if(!(obj instanceof String)){
                sendResponse(httpExchange, 500, "Invalid method return type");
            }
            message = obj.toString();
            sendResponse(httpExchange, 200, message);
        } catch (InvocationTargetException | IllegalAccessException e) {
            message = "Method couldn't be invoked";
            e.printStackTrace();
        }

        sendResponse(httpExchange, 500, message);
    }

    private void sendResponse(HttpExchange httpExchange, int httpStatusCode, String message) throws IOException {
        httpExchange.sendResponseHeaders(httpStatusCode, message.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }

}
