package com.clone.twitter.analytics_service.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String url;
    private String message;
}
