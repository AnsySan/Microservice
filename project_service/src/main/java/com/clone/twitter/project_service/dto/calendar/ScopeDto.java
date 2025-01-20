package com.clone.twitter.project_service.dto.calendar;

import com.clone.twitter.project_service.model.enums.calendar.AclScopeType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScopeDto {
    @NotNull(message = "Scope type is required property of ACL.")
    private AclScopeType type;

    private String value;
}
