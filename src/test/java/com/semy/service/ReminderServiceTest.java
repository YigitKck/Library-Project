package com.semy.service;

import com.semy.entities.Book;
import com.semy.entities.Rental;
import com.semy.entities.User;
import com.semy.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReminderServiceTest {

    private RentalRepository rentalRepository;
    private EmailService emailService;
    private ReminderService reminderService;

    @BeforeEach
    void setUp() throws Exception {
        rentalRepository = mock(RentalRepository.class);
        emailService = mock(EmailService.class);

        reminderService = new ReminderService();

        var repoField = ReminderService.class.getDeclaredField("rentalRepository");
        repoField.setAccessible(true);
        repoField.set(reminderService, rentalRepository);

        var emailField = ReminderService.class.getDeclaredField("emailService");
        emailField.setAccessible(true);
        emailField.set(reminderService, emailService);
    }

    @Test
    void testSendReminders() {
        User user = new User();
        user.setMail("user@example.com");

        Book book = new Book();
        book.setTitle("Effective Java");

        Rental rental1 = new Rental();
        rental1.setUser(user);
        rental1.setBook(book);
        rental1.setReturnDate(LocalDate.now().minusDays(5));
        rental1.setReminderSent(false);
        rental1.setReturned(false);

        List<Rental> rentals = List.of(rental1);

        when(rentalRepository.findByIsReturnedFalseAndReturnDateBeforeAndReminderSentFalse(any(LocalDate.class)))
                .thenReturn(rentals);

        int sentCount = reminderService.sendReminders();

        verify(emailService, times(1)).sendReminderEmail(
                eq("user@example.com"),
                eq("Effective Java"),
                eq(rental1.getReturnDate())
        );

        assertTrue(rental1.isReminderSent());
        verify(rentalRepository, times(1)).save(rental1);

        assertEquals(1, sentCount);
    }
}
