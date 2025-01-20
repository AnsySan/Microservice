package com.clone.twitter.post_service.kafka.event.comment;

import com.clone.twitter.post_service.kafka.event.KafkaEvent;
import com.clone.twitter.post_service.kafka.event.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class CommentEvent implements KafkaEvent {

    private Long id;
    private String content;
    private Long userId;
    private long likesCount;
    private Long postId;
    private State state;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
}
