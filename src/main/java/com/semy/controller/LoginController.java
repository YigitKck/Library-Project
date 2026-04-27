package com.semy.controller;
import com.semy.entities.User;
import com.semy.util.SessionUser;
import com.semy.util.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;

@Controller
@RestController
@Component
public class LoginController {

    @FXML
    private TextField userMail;

    @FXML
    private Label invalidMailText;

    @Autowired
    private UserController userController;

    // Giriş yaparken geçerli email kontrolü
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(gmail\\.com|hotmail\\.com|yahoo\\.com|outlook\\.com|icloud\\.com|yandex\\.com|protonmail\\.com|mail\\.com|zoho\\.com|gmx\\.com|ismu\\.edu\\.tr|medeniyet\\.edu\\.tr)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @FXML
    // Giriş yapma
    public void login(ActionEvent event) throws IOException {
        FXMLLoader loader = SpringFXMLLoader.load("/fxml/mainScreen.fxml");
        Parent root = loader.load();
        User user = new User();
        user.setMail(userMail.getText());
        MainScreenController mainController = loader.getController();
        mainController.setMail(user.getMail());

        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logMessage= "Gecersiz mail girildi";

        if (isValidEmail(user.getMail())) {
            logMessage = formattedDate + " " +user.getMail() + " giriş yaptı";
            userController.createUser(user);
            User savedUser = userController.createUser(user);
            SessionUser.getInstance().setUser(savedUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ana Sayfa");
            stage.show();
        }
        invalidMailText.setText("Lütfen geçerli bir mail adresi giriniz.");

        // Giriş yapıldığını email ile birlikte log a yazdırıyoruz
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}