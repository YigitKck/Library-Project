package com.semy.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
// Mail gönderme
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReminderEmail(String to, String bookTitle, LocalDate dueDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Kitap İade Hatırlatması");
        message.setText("Sayın kullanıcı,\n\n" + bookTitle + " adlı kitabın iade tarihi " + dueDate + " olarak gözüküyor. Lütfen en kısa sürede iade ediniz.");
        mailSender.send(message);
    }
}