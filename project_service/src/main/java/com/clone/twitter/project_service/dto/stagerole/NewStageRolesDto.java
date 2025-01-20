package com.clone.twitter.project_service.dto.stagerole;

import com.clone.twitter.project_service.model.TeamRole;
import com.clone.twitter.project_service.validation.enumvalidator.EnumValidator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewStageRolesDto {

    @NotNull(message = "TeamRole should not be null")
    @EnumValidator(enumClass = TeamRole.class, message = "Invalid Team Role")
    private String teamRole;

    @NotNull(message = "Count should not be null")
    @Positive(message = "Count should not be null")
    private Integer count;
}
