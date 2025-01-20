package com.clone.twitter.project_service.service.project;

import com.clone.twitter.project_service.dto.filter.ProjectFilterDto;
import com.clone.twitter.project_service.filter.ProjectFilter;
import com.clone.twitter.project_service.model.Project;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProjectFilterService {
    private final List<ProjectFilter> projectFilters;

    public Stream<Project> applyFilters(Stream<Project> projects, @NonNull ProjectFilterDto projectFilterDto) {
            projectFilters.stream()
                    .filter(projectFilter -> projectFilter.isApplicable(projectFilterDto))
                    .forEach(projectFilter ->projectFilter.apply(projects, projectFilterDto));
        return projects;
    }
}
