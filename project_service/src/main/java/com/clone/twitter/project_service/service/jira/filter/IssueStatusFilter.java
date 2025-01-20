package com.clone.twitter.project_service.service.jira.filter;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.clone.twitter.project_service.dto.jira.IssueFilterDto;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class IssueStatusFilter implements IssueFilter {

    @Override
    public boolean isAcceptable(IssueFilterDto filters) {
        return filters.getStatusId() != null;
    }

    @Override
    public Stream<Issue> apply(Stream<Issue> issues, IssueFilterDto filters) {
        return issues.filter(issue -> issue.getStatus().getId().equals(filters.getStatusId()));
    }
}
