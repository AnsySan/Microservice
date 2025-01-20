package com.clone.twitter.project_service.dto.calendar;

import com.clone.twitter.project_service.model.enums.calendar.AclRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AclDto {
    private String id;
    @NotNull(message = "Role is required property of Acl.")
    private AclRole role;
    @Valid
    @NotNull(message = "Scope is required property of Acl.")
    private ScopeDto scope;
}