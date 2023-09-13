package com.ucs.esporteconecta.view;

import java.net.URL;

public class ViewResourceHelper {
    private static final String WINDOW_FXML_FOLDER = "window/fxml/";
    private static final String CONTROLLER_SUFIX = "Controller";
    private static final String FXML_EXT = ".fxml";

    public static URL getWindowFxml(Class<?> clazz) {
        return ViewResourceHelper.class.getResource(WINDOW_FXML_FOLDER + windowControllerToFxml(clazz.getSimpleName()));
    }

    private static String windowControllerToFxml(String className) {
        if (className.contains(CONTROLLER_SUFIX))
            className = className.replace(CONTROLLER_SUFIX, "");

        className = fromCamelCaseToDash(className).toLowerCase();
        className += FXML_EXT;
        return className;
    }

    private static String fromCamelCaseToDash(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }

}
