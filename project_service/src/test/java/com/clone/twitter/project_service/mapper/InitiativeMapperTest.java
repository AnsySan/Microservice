package com.clone.twitter.project_service.mapper;

import com.clone.twitter.project_service.dto.initiative.InitiativeDto;
import com.clone.twitter.project_service.model.Project;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.initiative.Initiative;
import com.clone.twitter.project_service.model.initiative.InitiativeStatus;
import com.clone.twitter.project_service.model.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InitiativeMapperTest {
    @Spy
    private InitiativeMapper mapper = Mappers.getMapper(InitiativeMapper.class);

    private Initiative initiative;
    private InitiativeDto dto;

    @BeforeEach
    void init() {
        List<Stage> stages = List.of(
                Stage.builder().stageId(1L).build(),
                Stage.builder().stageId(2L).build(),
                Stage.builder().stageId(3L).build()
        );

        Project project = Project.builder().id(4L).build();

        TeamMember curator = TeamMember.builder().userId(5L).build();

        initiative = Initiative.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .status(InitiativeStatus.ACCEPTED)
                .stages(stages)
                .curator(curator)
                .project(project)
                .build();

        dto = InitiativeDto.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .status(InitiativeStatus.ACCEPTED)
                .stageIds(List.of(1L, 2L, 3L))
                .curatorId(5L)
                .projectId(4L)
                .build();
    }

    @Test
    void toEntity() {
        Initiative actual = mapper.toEntity(dto);
        assertEquals(initiative, actual);
    }

    @Test
    void toDto() {
        InitiativeDto actual = mapper.toDto(initiative);
        assertEquals(dto, actual);
    }
}