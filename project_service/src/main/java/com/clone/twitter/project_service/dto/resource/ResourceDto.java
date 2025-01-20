package com.clone.twitter.project_service.dto.resource;

import com.clone.twitter.project_service.model.ResourceStatus;
import com.clone.twitter.project_service.model.ResourceType;
import com.clone.twitter.project_service.model.TeamRole;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResourceDto {
    private Long id;
    private String key;
    private String name;
    private BigInteger size;
    private List<TeamRole> allowedRoles;
    private ResourceType type;
    private ResourceStatus status;
    private Long createdById;
    private Long updatedById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long projectId;
}
