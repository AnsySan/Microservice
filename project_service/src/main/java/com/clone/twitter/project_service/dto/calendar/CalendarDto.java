package com.clone.twitter.project_service.dto.calendar;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarDto {
    private String id;
    @NotNull(message = "Summary is required field of calendar.")
    private String summary;
    private String description;
    private String location;
}
