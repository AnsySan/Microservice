package com.clone.twitter.project_service.filter;

import com.clone.twitter.project_service.dto.filter.ProjectFilterDto;
import com.clone.twitter.project_service.model.Project;

import java.util.stream.Stream;

public interface ProjectFilter {
    boolean isApplicable(ProjectFilterDto projectFilterDto);

    Stream<Project> apply(Stream<Project> projects, ProjectFilterDto projectFilterDto);
}
