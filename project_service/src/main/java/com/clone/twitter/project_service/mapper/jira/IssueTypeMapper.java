package com.clone.twitter.project_service.mapper.jira;

import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.clone.twitter.project_service.dto.jira.IssueTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueTypeMapper {

    IssueTypeDto toDto(IssueType issueType);

    IssueType toEntity(IssueTypeDto issueTypeDto);
}