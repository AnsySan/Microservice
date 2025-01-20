package com.clone.twitter.user_service.mapper.jira;

import com.clone.twitter.user_service.dto.jira.JiraAccountDto;
import com.clone.twitter.user_service.entity.jira.JiraAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JiraAccountMapper {

    @Mapping(source = "user.id", target = "userId")
    JiraAccountDto toDto(JiraAccount jiraAccount);

    @Mapping(source = "userId", target = "user.id")
    JiraAccount toEntity(JiraAccountDto jiraAccountDto);
}
