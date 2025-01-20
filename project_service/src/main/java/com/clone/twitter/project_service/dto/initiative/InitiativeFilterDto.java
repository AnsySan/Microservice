package com.clone.twitter.project_service.dto.initiative;

import com.clone.twitter.project_service.model.initiative.InitiativeStatus;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitiativeFilterDto {

    @Positive(message = "curatorId should be positive")
    private Long curatorId;

    private InitiativeStatus status;
}
