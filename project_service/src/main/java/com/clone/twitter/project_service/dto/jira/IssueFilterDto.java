package com.clone.twitter.project_service.dto.jira;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssueFilterDto {
    private Long statusId;
    private String assigneeId;
}
