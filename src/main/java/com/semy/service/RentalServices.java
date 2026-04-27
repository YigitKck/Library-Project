package com.semy.service;

import com.semy.entities.Book;
import com.semy.entities.Rental;
import com.semy.entities.User;
import com.semy.repository.BookRepository;
import com.semy.repository.RentalRepository;
import com.semy.repository.UserRepository;
import javafx.scene.control.DatePicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class RentalServices {
    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookServices bookServices;

    public RentalServices(RentalRepository rentalRepository,
                          BookRepository bookRepository,
                          UserRepository userRepository,
                          BookServices bookServices) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookServices = bookServices;
    }
    public String rentBook(String mail, String bookTitle,LocalDate startDate, int days) {
        User user = userRepository.findByMail(mail)
                .orElseGet(() -> userRepository.save(new User(null, mail)));

        Book book = bookRepository.findByTitle(bookTitle).orElseGet(() -> {
            Book b = bookServices.searchBook(bookTitle);
            return bookRepository.save(b);
        });

        if (book.isRented()) {
            return "Bu kitap şu anda zaten kiralanmış.";
        }

        book.setRented(true);
        bookRepository.save(book);
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentDate(startDate);
        rental.setReturnDate(startDate.plusDays(days));
        rental.setReturned(false);
        rentalRepository.save(rental);
        return String.format("%s adlı kullanıcı '%s' kitabını %d günlüğüne kiraladı.",
                mail, book.getTitle(), days);
    }

    public void deleteRentalByBookTitle(String bookTitle) {
        Optional<Rental> rental = rentalRepository.findByBook_Title(bookTitle);
        rental.ifPresent(r -> {
            Book rentedBook = r.getBook();
            rentedBook.setRented(false);
            bookRepository.save(rentedBook);
            rentalRepository.delete(r);
        });
    }
}
