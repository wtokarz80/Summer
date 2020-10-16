package com.codecool.webroute;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Summer {
    final Map<String, Method> handlerMethods;

    public Summer() {
        handlerMethods = new HashMap<>();
    }

    /**
     * Accepts any number of handlers and extracts all methods annotated with {@link WebRoute}.
     * @param handlerClasses classes with methods annotated with {@link WebRoute}.
     */
    public void registerHandlers(Class<?>... handlerClasses) {

        Arrays.stream(handlerClasses)
                .forEach(e -> Arrays.stream(e.getDeclaredMethods())
                        .filter(q -> q.isAnnotationPresent(WebRoute.class))
                        .forEach(w -> handlerMethods.put(w.getAnnotation(WebRoute.class).path(), w)));

//        for(Class<?> handlerClass : handlerClasses){
//            Method[] methods = handlerClass.getDeclaredMethods();
//            for(Method method : methods){
//                if(method.isAnnotationPresent(WebRoute.class)){
//                    handlerMethods.put(method.getAnnotation(WebRoute.class).path(), method);
//                }
//            }
//        }
    }

    public void checkHandledPaths(){
        Set<Map.Entry<String, Method>> methodEntries = handlerMethods.entrySet();
        System.out.println("All paths that can be handled:");
        for (Map.Entry<String, Method> methodEntry : methodEntries) {
            System.out.println("PATH: " + methodEntry.getKey());
        }
    }

    /**
     * Starts HTTP server, waits for HTTP requests and redirects them to one of registered handler methods.
     */
    public void run() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(handlerMethods);
        server.createContext("/", httpRequestHandler::handle);
        server.setExecutor(null);
        server.start();
    }
}
