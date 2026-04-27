package com.semy.controller;
import com.semy.entities.Book;
import com.semy.service.BookServices;
import com.semy.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import com.semy.util.SetRentBookName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@Component
public class MainScreenController {

    private BookServices bookServices;

    private String mail;

    public void setMail(String mail) {
        this.mail = mail;
    }

    public MainScreenController() {
        this.bookServices = new BookServices();
    }

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button accountButton;

    @FXML
    private ListView<HBox> bookListView;

    private ObservableList<HBox> bookItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        bookListView.setItems(bookItems);
    }

    @FXML
    // Hesabım sayfasına gidiyoruz
    public void goToAccountPage(ActionEvent event) {
        try {
            FXMLLoader loader = SpringFXMLLoader.load("/fxml/account.fxml");
            Parent mainPage = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainPage));
            stage.setTitle("Ana Sayfa");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading account.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    // İsme göre kitap arama
    public void searchBook(ActionEvent event) {
        String query = searchField.getText().trim();

        if (!query.isEmpty()) {
            try {
                Book book = bookServices.searchBook(query);
                List<Book> foundBooks = new ArrayList<>();
                foundBooks.add(book);
                updateBookListView(foundBooks);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText("Kitap Bulunamadı");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText("Boş Arama");
            alert.setContentText("Lütfen bir kitap adı girin.");
            alert.showAndWait();
        }
    }

    @FXML
    // Kitabı aradıktan sonra kitapları gösterme
    private void updateBookListView(List<Book> books) {
        bookItems.clear();

        for (Book book : books) {
            HBox bookItem = createBookListItem(book);
            bookItems.add(bookItem);
        }
    }

    @FXML
    // Kitap ismini alarak kiralama ekranına gidiyoruz
    private void goToRentScreenWithBook(ActionEvent event, Book book) {
        try {
            SetRentBookName.getInstance().setSelectedBook(book);
            FXMLLoader loader = SpringFXMLLoader.load("/fxml/rentScreen.fxml");
            Parent rentPage = loader.load();

            RentScreenController controller = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(rentPage));
            stage.setTitle("Kitap Kirala: " + book.getTitle());
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading rentScreen.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Kitap ismini ve yanındaki kirala butonunu ekliyoruz
    private HBox createBookListItem(Book book) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label bookLabel = new Label(book.getTitle() + " - " + book.getAuthor());
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        Button rentButton = new Button("Kirala");
        rentButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        rentButton.setOnAction(event -> {
            try {
                goToRentScreenWithBook(event, book);
                System.out.println("Kiralama işlemi başlatıldı: " + book.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        hbox.getChildren().addAll(bookLabel, spacer, rentButton);
        return hbox;
    }
}