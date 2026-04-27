package com.semy.controller;
import com.semy.entities.Book;
import com.semy.service.ReminderService;
import com.semy.util.SessionUser;
import com.semy.util.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RestController
@Component
public class AccountController implements Initializable {

    @FXML
    public Button checkReturn;
    @FXML
    private Button homeButton;
    @Autowired
    private ReminderService reminderService;
    @FXML
    private TextField emailField;

    @Autowired
    private BookController bookController;

    @FXML
    private TableView<RentedBook> rentedBooksTable;
    @Autowired
    private RentalController rentalController;

    @Override
    // Kullanıcı bilgisini ve kiralanmış olan kitapları ekliyoruz
    public void initialize(URL url, ResourceBundle rb) {
        loadUserData();
        loadRentedBooks();
    }


    @FXML
    // Ana sayfaya dönüş
    public void goToHomePage(ActionEvent event) {
        try {
            FXMLLoader loader = SpringFXMLLoader.load("/fxml/mainScreen.fxml");
            Parent mainPage = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainPage));
            stage.setTitle("Ana Sayfa");
            stage.show();

        } catch (IOException e) {
            System.err.println("Ana sayfa yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUserData() {
        emailField.setText(SessionUser.getInstance().getUser().getMail());
    }

    @FXML
    // Gecikmiş kitaplar için mail gönderme
    private void checkReturn() {
        int count = reminderService.sendReminders();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hatırlatma");
        alert.setHeaderText("İşlem Tamamlandı");
        alert.setContentText(count + " kullanıcıya hatırlatma e-postası gönderildi.");
        alert.showAndWait();

        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage;

        // İade hatırlatma e-postası gönderildiğini log a yazdırıyoruz
        if (count != 0) {
            logMessage = formattedDate + " " + count + " " + "kullanıcıya hatırlatma e-postası gönderildi.";
        } else {
            logMessage = formattedDate + " " + "hatırlatma e-postası gönderilecek bir durum bulunamadı.";
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Kiralanmış olan kitapları database den yüklüyoruz
    private void loadRentedBooks() {

        List<Book> bookList = bookController.getAllBooks();
        ObservableList<RentedBook> rentedBooks = FXCollections.observableArrayList();
        for (Book book : bookList) {
            rentedBooks.add(new RentedBook(book.getTitle(), book.getAuthor(), book.getReturnDate().toString()));
        }
        for (Book book :  bookController.getAllBooks()) {
            System.out.println(book.getTitle());
        }
        TableColumn<RentedBook, String> nameColumn = (TableColumn<RentedBook, String>) rentedBooksTable.getColumns().get(0);
        TableColumn<RentedBook, String> authorColumn = (TableColumn<RentedBook, String>) rentedBooksTable.getColumns().get(1);
        TableColumn<RentedBook, String> returnDateColumn = (TableColumn<RentedBook, String>) rentedBooksTable.getColumns().get(2);
        TableColumn<RentedBook, Button> actionsColumn = (TableColumn<RentedBook, Button>) rentedBooksTable.getColumns().get(3);

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        returnDateColumn.setCellValueFactory(cellData -> cellData.getValue().returnDateProperty());

        actionsColumn.setCellFactory(col -> new TableCell<RentedBook, Button>() {
            private final Button returnButton = new Button("İade Et");

            {
                returnButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
                returnButton.setOnAction(event -> {
                    RentedBook book = getTableView().getItems().get(getIndex());
                    System.out.println(book.getName() + " kitabı iade edildi.");
                    rentalController.deleteRentalByBookTitle(book.getName());
                    bookController.deleteBook(book.getName());
                    loadRentedBooks();
                });
            }
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(returnButton);
                }
            }
        });
        rentedBooksTable.setItems(rentedBooks);
    }
    public static class RentedBook {
        private final javafx.beans.property.StringProperty name;
        private final javafx.beans.property.StringProperty author;
        private final javafx.beans.property.StringProperty returnDate;

        public RentedBook(String name, String author, String returnDate) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.author = new javafx.beans.property.SimpleStringProperty(author);
            this.returnDate = new javafx.beans.property.SimpleStringProperty(returnDate.toString());
        }

        public javafx.beans.property.StringProperty nameProperty() {
            return name;
        }

        public javafx.beans.property.StringProperty authorProperty() {
            return author;
        }

        public javafx.beans.property.StringProperty returnDateProperty() {
            return returnDate;
        }

        public String getName() {
            return name.get();
        }
    }
}