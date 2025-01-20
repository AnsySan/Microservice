package com.clone.twitter.user_service.service.jira;


import com.clone.twitter.user_service.dto.jira.JiraAccountDto;

public interface JiraAccountService {
    JiraAccountDto addJiraAccount(long userId, JiraAccountDto jiraAccountDto);

    JiraAccountDto getJiraAccountInfo(long userId);
}
