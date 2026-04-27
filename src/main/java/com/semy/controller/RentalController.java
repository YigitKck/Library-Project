package com.semy.controller;

import com.semy.service.RentalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalServices rentalServices;

    @Autowired
    public RentalController(RentalServices rentalServices) {
        this.rentalServices = rentalServices;
    }

    @PostMapping
    // Kitabı kiralama fonksiyonu
    public String rentBook(
            @RequestParam String mail,
            @RequestParam String bookTitle,
            @RequestParam LocalDate startDate,
            @RequestParam int days
    ) {
        return rentalServices.rentBook(mail, bookTitle,startDate, days);
    }
    @DeleteMapping("/by-title")
    // Kitap ismi ile database den kitap silme
    public void deleteRentalByBookTitle(@RequestParam String bookTitle) {
        rentalServices.deleteRentalByBookTitle(bookTitle);
        // Kitap iadesini log a yazdırır
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = formattedDate + " " + bookTitle + " " + "adlı kitabın iade işlemi gerçekleşmiştir";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

