package com.clone.twitter.project_service.service.jira;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.clone.twitter.project_service.dto.jira.IssueFilterDto;

import java.util.stream.Stream;

public interface JiraFilterService {
    Stream<Issue> applyAll(Stream<Issue> issues, IssueFilterDto issueFilterDto);
}
