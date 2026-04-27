package com.semy.proje;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import com.semy.util.SpringFXMLLoader;

public class JavaFxLauncher extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = ProjeApplication.startSpringApplication();
        SpringFXMLLoader.setApplicationContext(context);
    }

    @Override
    // Login ekranının yükleme
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = SpringFXMLLoader.load("/fxml/login.fxml");
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Kütüphane Sistemi - Giriş");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}