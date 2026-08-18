package com.umair.banking.notification.service;

import com.umair.banking.notification.dto.EmailNotification;

public interface EmailService {

    void sendEmail(EmailNotification notification);
}
