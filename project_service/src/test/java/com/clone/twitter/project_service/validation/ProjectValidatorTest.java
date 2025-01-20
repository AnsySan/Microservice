package com.clone.twitter.project_service.validation;

import com.clone.twitter.project_service.dto.project.ProjectDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.repository.ProjectRepository;
import com.clone.twitter.project_service.validation.project.impl.ProjectValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectValidatorTest {
    @InjectMocks
    private ProjectValidatorImpl projectValidator;
    @Mock
    private ProjectRepository projectRepository;
    private ProjectDto firstProjectDto;

    @BeforeEach
    public void init() {
        firstProjectDto = new ProjectDto();
        firstProjectDto.setId(1L);
        firstProjectDto.setName("First project");
    }

    @Test
    public void testValidateProjectByOwnerIdAndNameOfProjectWithException() {
        when(projectRepository.existsByOwnerIdAndName(firstProjectDto.getOwnerId(), firstProjectDto.getName())).thenReturn(true);
        var exception = assertThrows(DataValidationException.class, () -> projectValidator.validateProjectByOwnerIdAndNameOfProject(firstProjectDto));
        assertEquals("The user with id: "+firstProjectDto.getOwnerId()+ " already has a project with name "+firstProjectDto.getName(), exception.getMessage());
    }
}
