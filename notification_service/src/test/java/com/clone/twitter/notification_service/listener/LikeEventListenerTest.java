package com.clone.twitter.notification_service.listener;

import com.clone.twitter.notification_service.client.UserServiceClient;
import com.clone.twitter.notification_service.dto.UserDto;
import com.clone.twitter.notification_service.entity.PreferredContact;
import com.clone.twitter.notification_service.event.LikeEvent;
import com.clone.twitter.notification_service.listener.like.LikeEventListener;
import com.clone.twitter.notification_service.messaging.LikeMessageBuilder;
import com.clone.twitter.notification_service.messaging.MessageBuilder;
import com.clone.twitter.notification_service.service.NotificationService;
import com.clone.twitter.notification_service.service.email.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    private LikeEventListener likeEventListener;
    private MessageBuilder<LikeEvent> likeMessageBuilder;
    private UserServiceClient userServiceClient;
    private List<NotificationService> notificationServices = new ArrayList<>();
    private ObjectMapper objectMapper;
    private LikeEvent likeEvent;
    private UserDto userDto;
    private Locale locale = Locale.ENGLISH;
    private EmailService emailService;
    private String message = "test";

    @BeforeEach
    void init() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        userServiceClient = Mockito.mock(UserServiceClient.class);
        likeMessageBuilder = Mockito.mock(LikeMessageBuilder.class);
        emailService = Mockito.mock(EmailService.class);
        notificationServices.add(emailService);
        likeEventListener = new LikeEventListener(objectMapper, likeMessageBuilder, notificationServices, userServiceClient);

        likeEvent = LikeEvent.builder()
                .authorId(1L)
                .userId(2L)
                .postId(1L)
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .preferredContact(PreferredContact.EMAIL)
                .build();

        Locale.setDefault(locale);
    }

    @Test
    public void testOnMessageWithSuccessfulHandlingEvent() throws IOException {
        byte[] body = new byte[]{2};
        Message springMessage = new DefaultMessage(new byte[]{1}, body);
        byte[] pattern = new byte[]{3};

        when(objectMapper.readValue(springMessage.getBody(), LikeEvent.class)).thenReturn(likeEvent);
        when(likeMessageBuilder.buildMessage(likeEvent, locale)).thenReturn(message);
        when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);
        notificationServices.forEach(notificationService ->
                when(notificationService.getPreferredContact()).thenCallRealMethod());
        doNothing().when(emailService).send(userDto, message);

        likeEventListener.onMessage(springMessage, pattern);

        InOrder inOrder = Mockito.inOrder(objectMapper, likeMessageBuilder, userServiceClient, emailService);
        inOrder.verify(objectMapper, times(1)).readValue(springMessage.getBody(), LikeEvent.class);
        inOrder.verify(likeMessageBuilder, times(1)).buildMessage(likeEvent, locale);
        notificationServices.forEach(notificationService ->
                inOrder.verify(notificationService, times(1)).getPreferredContact());
        inOrder.verify(emailService, times(1)).send(userDto, message);
    }
}
