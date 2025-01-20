package com.clone.twitter.project_service.mapper;

import com.clone.twitter.project_service.dto.initiative.InitiativeDto;
import com.clone.twitter.project_service.model.initiative.Initiative;
import com.clone.twitter.project_service.model.stage.Stage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InitiativeMapper {

    @Mapping(source = "curatorId", target = "curator.userId")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "stageIds", target = "stages", qualifiedByName = "getStageFromId")
    Initiative toEntity(InitiativeDto initiativeDto);

    @Mapping(source = "curator.userId", target = "curatorId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "stages", target = "stageIds", qualifiedByName = "getIdFromStage")
    InitiativeDto toDto(Initiative initiative);

    @Named("getIdFromStage")
    default Long getIdFromStage(Stage stage) {
        return stage.getStageId();
    }

    @Named("getStageFromId")
    default Stage getStageFromId(Long id) {
        return Stage.builder().stageId(id).build();
    }
}
