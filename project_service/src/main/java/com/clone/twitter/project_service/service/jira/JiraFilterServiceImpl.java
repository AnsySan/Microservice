package com.clone.twitter.project_service.service.jira;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.clone.twitter.project_service.dto.jira.IssueFilterDto;
import com.clone.twitter.project_service.service.jira.filter.IssueFilter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JiraFilterServiceImpl implements JiraFilterService {

    private final List<IssueFilter> filters;

    @Override
    public Stream<Issue> applyAll(Stream<Issue> issues, @NonNull IssueFilterDto issueFilterDto) {
        return filters.stream()
                .filter(filter -> filter.isAcceptable(issueFilterDto))
                .flatMap(filter -> filter.apply(issues, issueFilterDto));
    }
}
