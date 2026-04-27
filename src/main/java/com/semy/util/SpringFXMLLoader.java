package com.semy.util;
import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;

public class SpringFXMLLoader {
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {

        context = applicationContext;
    }
    public static FXMLLoader load(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean); // spring in kontrolü
        loader.setLocation(SpringFXMLLoader.class.getResource(fxmlPath));
        return loader;
    }
}
