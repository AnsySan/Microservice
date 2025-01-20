package com.clone.twitter.project_service.filter;

import com.clone.twitter.project_service.dto.filter.ProjectFilterDto;
import com.clone.twitter.project_service.model.Project;
import com.clone.twitter.project_service.model.ProjectStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProjectStatusFilterTest {
    @Spy
    private ProjectStatusFilter projectStatusFilter;

    private ProjectFilterDto projectFilterDto;
    private Project project;

    @BeforeEach
    void init() {
        projectFilterDto = ProjectFilterDto.builder().status(ProjectStatus.CREATED).build();
        project = Project.builder().status(ProjectStatus.CREATED).build();
    }

    @Test
    public void testIsApplicableGood() {
        assertTrue(projectStatusFilter.isApplicable(projectFilterDto));
    }

    @Test
    public void testIaApplicableBad() {
        projectFilterDto.setStatus(null);
        assertFalse(projectStatusFilter.isApplicable(projectFilterDto));
    }

    @Test
    public void testApply() {
        List<Project> result = projectStatusFilter.apply(Stream.of(project), projectFilterDto).toList();
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), project);
    }
}
