package com.clone.twitter.project_service.mapper;

import com.clone.twitter.project_service.dto.moment.MomentDto;
import com.clone.twitter.project_service.model.Moment;
import com.clone.twitter.project_service.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MomentMapper {

    @Mapping(source = "projectIds", target = "projects", qualifiedByName = "getProjectFromId")
    Moment toEntity(MomentDto momentDto);

    @Mapping(source = "projects", target = "projectIds", qualifiedByName = "getIdFromProject")
    MomentDto toDto(Moment moment);

    @Named("getIdFromProject")
    default Long getIdFromProject(Project project) {
        return project.getId();
    }

    @Named("getProjectFromId")
    default Project getProjectFromId(Long id) {
        return Project.builder().id(id).build();
    }
}
