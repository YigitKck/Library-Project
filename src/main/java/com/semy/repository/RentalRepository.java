package com.semy.repository;
import com.semy.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByBook_Title(String title);
    List<Rental> findByIsReturnedFalseAndReturnDateBeforeAndReminderSentFalse(LocalDate date);
}
