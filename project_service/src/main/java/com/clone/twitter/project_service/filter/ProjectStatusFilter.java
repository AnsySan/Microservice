package com.clone.twitter.project_service.filter;

import com.clone.twitter.project_service.dto.filter.ProjectFilterDto;
import com.clone.twitter.project_service.model.Project;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class ProjectStatusFilter implements ProjectFilter {
    @Override
    public boolean isApplicable(ProjectFilterDto projectFilterDto) {
        return projectFilterDto.getStatus() != null;
    }

    @Override
    public Stream<Project> apply(Stream<Project> projects, ProjectFilterDto projectFilterDto) {
        return projects.filter(project -> project.getStatus().equals(projectFilterDto.getStatus()));
    }
}
