package com.clone.twitter.project_service.validation.project;

import com.clone.twitter.project_service.dto.project.ProjectDto;

public interface ProjectValidator {
    void validateProjectByOwnerIdAndNameOfProject(ProjectDto projectDto);

    void validateProjectExistence(long id);

    void validateProjectIdAndCurrentUserId(long projectId, long currentUserId);
}
