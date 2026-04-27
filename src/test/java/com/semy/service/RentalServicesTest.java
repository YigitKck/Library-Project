package com.semy.service;

import com.semy.entities.Book;
import com.semy.entities.Rental;
import com.semy.entities.User;
import com.semy.repository.BookRepository;
import com.semy.repository.RentalRepository;
import com.semy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentalServicesTest {

    private RentalServices rentalServices;
    private BookRepository bookRepository;
    private RentalRepository rentalRepository;
    private UserRepository userRepository;
    private BookServices bookServices;

    @BeforeEach
    void setup() {
        bookRepository = mock(BookRepository.class);
        rentalRepository = mock(RentalRepository.class);
        userRepository = mock(UserRepository.class);
        bookServices = mock(BookServices.class);

        rentalServices = new RentalServices(
                rentalRepository,
                bookRepository,
                userRepository,
                bookServices
        );
    }


    @Test
    void testRentBook_Success() {
        String mail = "test@mail.com";
        String bookTitle = "Test Kitabı";
        LocalDate startDate = LocalDate.now();
        int days = 7;

        User user = new User();
        user.setMail(mail);
        Book book = new Book();
        book.setTitle(bookTitle);
        book.setRented(false);

        when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
        when(bookRepository.findByTitle(bookTitle)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        String result = rentalServices.rentBook(mail, bookTitle,startDate, days);

        assertTrue(result.contains("kiraladı"));
        verify(rentalRepository, times(1)).save(any(Rental.class));
        assertTrue(book.isRented());
    }

    @Test
    void testRentBook_BookAlreadyRented() {
        String mail = "test@mail.com";
        String bookTitle = "Test Kitabı";
        LocalDate startDate = LocalDate.now();

        User user = new User();
        user.setMail(mail);
        Book book = new Book();
        book.setTitle(bookTitle);
        book.setRented(true);

        when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
        when(bookRepository.findByTitle(bookTitle)).thenReturn(Optional.of(book));

        String result = rentalServices.rentBook(mail, bookTitle,startDate, 5);

        assertEquals("Bu kitap şu anda zaten kiralanmış.", result);
        verify(rentalRepository, never()).save(any());
    }
}
