package com.clone.twitter.notification_service.dto.notification;

import com.clone.twitter.notification_service.dto.UserDto;
import lombok.Data;

@Data
public class NotificationRequest {
    private UserDto user;
    private String message;
}
