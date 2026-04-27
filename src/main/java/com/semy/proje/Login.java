package com.semy.proje;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import java.io.IOException;

public class Login extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(ProjeApplication.class).run();
    }

    @Override
    // Login ekranı burada yükleniyor
    public void start(Stage loginStage) throws IOException {
        FXMLLoader login = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        login.setControllerFactory(context::getBean);
        Scene loginScene = new Scene(login.load());
        loginStage.setScene(loginScene);
        loginStage.setTitle("Giriş Yap");
        loginStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
