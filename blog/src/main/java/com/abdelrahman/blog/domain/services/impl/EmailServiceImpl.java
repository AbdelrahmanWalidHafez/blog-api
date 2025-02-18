package com.abdelrahman.blog.domain.services.impl;
import com.abdelrahman.blog.domain.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;
    @Override
    public void sendEmail(String email) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("walidlrahmn5@gmail.com");
        message.setTo(email);
        message.setText("You have been logged successfully to my Blog website");
        message.setSubject("Blog");
        mailSender.send(message);
    }
}
