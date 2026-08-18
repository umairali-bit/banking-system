package com.umair.banking.notification.dto;

public record EmailNotification(

        String to,
        String subject,
        String body

) {
}
