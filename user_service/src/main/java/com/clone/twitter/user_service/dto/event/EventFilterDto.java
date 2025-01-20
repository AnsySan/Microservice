package com.clone.twitter.user_service.dto.event;

import com.clone.twitter.user_service.dto.skill.SkillDto;
import com.clone.twitter.user_service.entity.event.EventStatus;
import com.clone.twitter.user_service.entity.event.EventType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFilterDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<SkillDto> relatedSkills;

    @Size(max = 128, message = "location should be less than 129 symbols")
    private String location;

    private EventType type;
    private EventStatus status;
}
