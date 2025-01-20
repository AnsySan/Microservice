package com.clone.twitter.project_service.mapper;

import com.clone.twitter.project_service.dto.stage.NewStageDto;
import com.clone.twitter.project_service.dto.stage.StageDto;
import com.clone.twitter.project_service.model.Task;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.stage.Stage;
import com.clone.twitter.project_service.model.stage.StageRoles;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = StageRolesMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StageMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "stageRoles", target = "stageRolesIds", qualifiedByName = "stageRolesToStageRolesIds")
    @Mapping(source = "tasks", target = "tasksIds", qualifiedByName = "tasksToTasksIds")
    @Mapping(source = "executors", target = "executorsIds", qualifiedByName = "executorsToExecutorsIds")
    StageDto toDto(Stage entity);

    @Mapping(target = "stageId", ignore = true)
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "tasksIds", target = "tasks", qualifiedByName = "taskIdsToTasks")
    @Mapping(source = "executorsIds", target = "executors", qualifiedByName = "executorIdsToExecutors")
    Stage toEntity(NewStageDto dto);

    List<StageDto> toDtoList(List<Stage> entities);

    @Named("stageRolesToStageRolesIds")
    default List<Long> stageRolesToStageRolesIds(List<StageRoles> stageRoles) {
        return stageRoles.stream()
                .map(StageRoles::getId)
                .toList();
    }

    @Named("tasksToTasksIds")
    default List<Long> tasksToTasksIds(List<Task> tasks) {
        return tasks.stream()
                .map(Task::getId)
                .toList();
    }

    @Named("taskIdsToTasks")
    default List<Task> taskIdsToTasks(List<Long> taskIds) {
        return taskIds.stream()
                .map(id -> Task.builder().id(id).build())
                .toList();
    }

    @Named("executorsToExecutorsIds")
    default List<Long> executorsToExecutorsIds(List<TeamMember> executors) {
        return executors.stream()
                .map(TeamMember::getId)
                .toList();
    }

    @Named("executorIdsToExecutors")
    default List<TeamMember> executorIdsToExecutors(List<Long> executorIds) {
        return executorIds.stream()
                .map(id -> TeamMember.builder().id(id).build())
                .toList();
    }
}
