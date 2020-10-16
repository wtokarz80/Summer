package com.codecool.webroute;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class App {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        Summer summer = new Summer();
        summer.registerHandlers(ExampleHandler.class);
        summer.checkHandledPaths();
        summer.run();
    }
}
