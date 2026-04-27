package com.semy.controller;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.semy.entities.Book;
import com.semy.repository.RentalRepository;
import com.semy.repository.UserRepository;
import com.semy.service.ExcelReportService;
import com.semy.util.SessionUser;
import com.semy.util.SetRentBookName;
import com.semy.util.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.stereotype.Component;
import com.semy.controller.LoginController;
import com.semy.controller.BookController;
import javafx.scene.control.Label;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@Component
public class RentScreenController {

    @Autowired
    private RentalController rentalController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookController bookController;

    @Autowired
    private ExcelReportService excelReportService;

    @FXML
    private Label bookTitleLabel;

    @FXML
    private Label bookAuthorLabel;

    @FXML
    private ComboBox rentalDurationCombo;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    // Ana sayfada dönüş
    public void goToHomePage(ActionEvent event) {
        try {
            FXMLLoader loader = SpringFXMLLoader.load("/fxml/mainScreen.fxml"); // 🔄 DEĞİŞTİRİLDİ
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

    @FXML
    public void initialize() {
        Book selectedBook = SetRentBookName.getInstance().getSelectedBook();
        if (selectedBook != null) {
            bookTitleLabel.setText(selectedBook.getTitle());
            bookAuthorLabel.setText(selectedBook.getAuthor());
        }
    }

    @FXML
    // Kitap kiralama fonksiyonu
    public void confirmRent(ActionEvent event) throws Exception {
        String mail = SessionUser.getInstance().getUser().getMail();
        Book selectedBook = SetRentBookName.getInstance().getSelectedBook();

        if (selectedBook != null) {
            String rentalDuration = rentalDurationCombo.getValue().toString();
            System.out.println(rentalDuration);
            LocalDate rentStartDate = startDatePicker.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            LocalDate yeniTarih = rentStartDate.plusDays(Integer.parseInt(rentalDuration));
            selectedBook.setReturnDate(yeniTarih);

            bookController.createBook(selectedBook);
            rentalController.rentBook(mail, selectedBook.getTitle(),startDatePicker.getValue(),Integer.parseInt(rentalDuration));
            // Excel dosyasına kitabın kiralandığını yazdırıyoruz
            excelReportService.generateLoanReport("rapor.xlsx",mail,selectedBook, yeniTarih.format(formatter));

            String logMessage= "Kiralanan kitap: " + selectedBook.getTitle() + " iade tarihi: " + yeniTarih.format(formatter);

            // Kiralanan kitabı log a yazdırıyoruz
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                writer.write(logMessage);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

