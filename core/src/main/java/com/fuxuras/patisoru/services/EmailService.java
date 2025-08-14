package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.entities.VerificationToken;
import com.fuxuras.patisoru.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final VerificationTokenRepository verificationTokenRepository;

    @Value("${spring.mail.username}")
    private String from;

    public void sendAuthMail(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.createVerificationToken(); // This should generate the 6-digit code
        verificationTokenRepository.save(verificationToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getEmail());
        message.setSubject("Patisoru Email Doğrulama Kodunuz"); // More descriptive subject

        String verificationCode = verificationToken.getToken();

        String verificationUrl = "https://patisoru.com.tr/email-verification?email=" + user.getEmail() + "&code=" + verificationCode;

        String emailBody = "Merhaba " + user.getFullName() + ",\n\n" // Optional: Add user's name
                + "Patisoru hesabınız için email adresinizi doğrulamak amacıyla bu email'i aldınız.\n\n"
                + "Doğrulama Kodunuz: " + verificationCode + "\n\n" // Display the code clearly
                + "Email adresinizi doğrulamak için lütfen aşağıdaki bağlantıya tıklayın ve sayfada istenen alana yukarıdaki kodu girin:\n"
                + verificationUrl + "\n\n" // Link to the verification page
                + "Bu kodu siz talep etmediyseniz lütfen bu e-postayı dikkate almayın.\n\n"
                + "Teşekkürler,\n"
                + "Patisoru Ekibi";


        message.setText(emailBody);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send verification email to " + user.getEmail() + ": " + e.getMessage());
        }
    }
}
