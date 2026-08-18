package com.umair.banking.notification.service.impl;

import com.umair.banking.notification.dto.EmailNotification;
import com.umair.banking.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendEmail(EmailNotification notification) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.to());
        message.setSubject(notification.subject());
        message.setText(notification.body());

        mailSender.send(message);

    }
}
