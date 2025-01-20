package com.clone.twitter.notification_service.service;

import com.clone.twitter.notification_service.dto.UserDto;
import com.clone.twitter.notification_service.entity.PreferredContact;

public interface NotificationService {

    void send(UserDto user, String message);

    PreferredContact getPreferredContact();
}
