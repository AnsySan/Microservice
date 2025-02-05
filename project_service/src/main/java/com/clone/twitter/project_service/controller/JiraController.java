package com.clone.twitter.project_service.controller;

import com.clone.twitter.project_service.dto.jira.IssueDto;
import com.clone.twitter.project_service.dto.jira.IssueFilterDto;
import com.clone.twitter.project_service.service.jira.JiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Jira Issues")
@RestController
@RequestMapping(("/jira"))
@RequiredArgsConstructor
public class JiraController {

    private final JiraService jiraService;

    @PostMapping("/issues")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create new issue",
            parameters = {@Parameter(in = ParameterIn.HEADER, name = "x-user-id", required = true)}
    )
    public String createIssue(@Valid @RequestBody IssueDto issueDto) {
        return jiraService.createIssue(issueDto);
    }

    @GetMapping("/issues/{issueKey}")
    @Operation(
            summary = "Get issue by issueKey",
            parameters = {@Parameter(in = ParameterIn.HEADER, name = "x-user-id", required = true)}
    )
    public IssueDto getIssue(@PathVariable String issueKey) {
        return jiraService.getIssue(issueKey);
    }

    @GetMapping("/{projectKey}/issues")
    @Operation(
            summary = "Get all project issues",
            parameters = {@Parameter(in = ParameterIn.HEADER, name = "x-user-id", required = true)}
    )
    public List<IssueDto> getAllIssues(@PathVariable String projectKey) {
        return jiraService.getAllIssues(projectKey);
    }

    @GetMapping("/{projectKey}/issues/filter")
    @Operation(
            summary = "Get all project issues by filter",
            parameters = {@Parameter(in = ParameterIn.HEADER, name = "x-user-id", required = true)}
    )
    public List<IssueDto> getIssuesByFilter(@PathVariable String projectKey, IssueFilterDto issueFilterDto) {
        return jiraService.getIssuesByFilter(projectKey, issueFilterDto);
    }
}
