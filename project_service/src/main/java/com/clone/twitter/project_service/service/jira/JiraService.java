package com.clone.twitter.project_service.service.jira;

import com.clone.twitter.project_service.dto.jira.IssueDto;
import com.clone.twitter.project_service.dto.jira.IssueFilterDto;

import java.util.List;

public interface JiraService {

    String createIssue(IssueDto issueDto);

    IssueDto getIssue(String issueKey);

    List<IssueDto> getAllIssues(String projectKey);

    List<IssueDto> getIssuesByFilter(String projectKey, IssueFilterDto issueFilterDto);
}
