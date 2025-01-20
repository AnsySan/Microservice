package com.clone.twitter.project_service.service.project;

import com.clone.twitter.project_service.dto.filter.ProjectFilterDto;
import com.clone.twitter.project_service.dto.project.ProjectDto;

import java.util.List;

public interface ProjectService {

    ProjectDto create(ProjectDto projectDto);

    ProjectDto update(ProjectDto projectDto);

    List<ProjectDto> getAll();

    ProjectDto findById(long id);

    List<ProjectDto> getAllByFilter(ProjectFilterDto projectFilterDto);

    boolean isUserProjectOwner(long projectId, long userId);
}
