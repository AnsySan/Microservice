package com.clone.twitter.project_service.validation.task.impl;

import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.repository.TaskRepository;
import com.clone.twitter.project_service.validation.task.TaskValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskValidatorImpl implements TaskValidator {
    private final TaskRepository taskRepository;

    @Override
    public void validateTaskExistence(long id) {
        boolean exists = taskRepository.existsById(id);
        if (!exists) {
            var message = String.format("a task with %d does not exist", id);

            throw new DataValidationException(message);
        }
    }
}
