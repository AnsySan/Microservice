package com.clone.twitter.project_service.validation.project.impl;

import com.clone.twitter.project_service.dto.project.ProjectDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.repository.ProjectRepository;
import com.clone.twitter.project_service.validation.project.ProjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectValidatorImpl implements ProjectValidator {
    private final ProjectRepository projectRepository;

    @Override
    public void validateProjectByOwnerIdAndNameOfProject(ProjectDto projectDto) {
        if (projectRepository.existsByOwnerIdAndName(projectDto.getOwnerId(), projectDto.getName())) {
            throw new DataValidationException("The user with id: " + projectDto.getOwnerId() + " already has a project with name " + projectDto.getName());
        }
    }

    @Override
    public void validateProjectExistence(long id) {
        boolean exists = projectRepository.existsById(id);
        if (!exists) {
            String message = String.format("a project with id %d does not exist", id);

            throw new DataValidationException(message);
        }
    }

    @Override
    public void validateProjectIdAndCurrentUserId(long projectId, long currentUserId) {
        if (!projectRepository.checkUserIsProjectOwner(projectId, currentUserId)) {
            String message = String.format("a user with id %d does not owe a project with id %d", currentUserId, projectId);


            throw new DataValidationException(message);
        }

    }
}
