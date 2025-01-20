package com.clone.twitter.project_service.service.moment;

import com.clone.twitter.project_service.dto.moment.MomentDto;
import com.clone.twitter.project_service.mapper.MomentMapper;
import com.clone.twitter.project_service.model.Moment;
import com.clone.twitter.project_service.model.Project;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.initiative.Initiative;
import com.clone.twitter.project_service.repository.MomentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MomentServiceImpl implements MomentService {
    private final MomentMapper mapper;
    private final MomentRepository momentRepository;

    @Override
    public MomentDto create(MomentDto moment) {
        Moment entity = mapper.toEntity(moment);
        Moment saved = momentRepository.save(entity);

        return mapper.toDto(saved);
    }

    @Override
    public MomentDto createFromInitiative(Initiative initiative) {
        MomentDto momentDto = new MomentDto();

        momentDto.setName(initiative.getName());
        momentDto.setDescription(momentDto.getDescription());

        List<Long> projectIds = initiative.getSharingProjects().stream()
                .map(Project::getId)
                .toList();

        momentDto.setProjectIds(projectIds);

        List<Long> userIds = initiative.getStages().stream()
                .flatMap(stage -> stage.getExecutors().stream())
                .map(TeamMember::getUserId)
                .toList();

        momentDto.setUserIds(userIds);

        return create(momentDto);
    }
}
