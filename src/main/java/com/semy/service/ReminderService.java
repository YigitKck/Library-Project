package com.semy.service;
import com.semy.entities.Rental;
import com.semy.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private EmailService emailService;

    public int sendReminders() {
        List<Rental> overdueRentals = rentalRepository
                .findByIsReturnedFalseAndReturnDateBeforeAndReminderSentFalse(LocalDate.now());

        for (Rental rental : overdueRentals) {
            emailService.sendReminderEmail(
                    rental.getUser().getMail(),
                    rental.getBook().getTitle(),
                    rental.getReturnDate()
            );
            rental.setReminderSent(true);
            rentalRepository.save(rental);
        }
        return overdueRentals.size();
    }
}
