package com.clone.twitter.account_service.client;

import com.clone.twitter.account_service.dto.ProjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "project-service", url = "${services.project-service.host}:${services.project-service.port}")
public interface ProjectServiceClient {

    @GetMapping("${api.version}/projects/{projectId}")
    ProjectDto getProject(@PathVariable("projectId") long projectId);

    @PostMapping("${api.version}/projects/by-ids")
    List<ProjectDto> getProjectsByIds(@RequestBody List<Long> ids);

    @GetMapping("${api.version}/projects/{projectId}/owner/{userId}")
    boolean checkProjectOwner(@PathVariable("projectId") long projectId,
                              @PathVariable("userId") long userId);
}
