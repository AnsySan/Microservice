package com.clone.twitter.project_service.service.stage_invitation;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationCreateDto;
import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationDto;
import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationFilterDto;
import com.clone.twitter.project_service.exceptions.NotFoundException;
import com.clone.twitter.project_service.mapper.StageInvitationMapper;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.stage.Stage;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitation;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitationStatus;
import com.clone.twitter.project_service.publisher.InviteSentPublisher;
import com.clone.twitter.project_service.repository.StageInvitationRepository;
import com.clone.twitter.project_service.repository.StageRepository;
import com.clone.twitter.project_service.repository.TeamMemberRepository;
import com.clone.twitter.project_service.validation.stage_invitation.StageInvitationValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StageInvitationServiceImpl implements StageInvitationService {

    private final StageInvitationRepository stageInvitationRepository;
    private final StageInvitationMapper stageInvitationMapper;
    private final TeamMemberRepository teamMemberRepository;
    private final StageRepository stageRepository;
    private final StageInvitationValidator stageInvitationValidator;
    private final StageInvitationFilterService stageInvitationFilterService;
    private final InviteSentPublisher inviteSentPublisher;

    @Override
    @Transactional
    public StageInvitationDto sendInvitation(StageInvitationCreateDto stageInvitationCreateDto) {

        StageInvitation stageInvitation = stageInvitationMapper.toEntity(stageInvitationCreateDto);

        stageInvitationValidator.validateExistences(stageInvitationCreateDto);

        long projectId = stageRepository.getById(stageInvitationCreateDto.getStageId()).getProject().getId();
        inviteSentPublisher.publish(stageInvitationMapper.toEvent(stageInvitationCreateDto, projectId));

        return stageInvitationMapper.toDto(stageInvitationRepository.save(stageInvitation));
    }

    @Override
    @Transactional
    public StageInvitationDto acceptInvitation(long userId, long inviteId) {

        TeamMember invited = findTeamMemberById(userId);
        StageInvitation stageInvitation = findStageInvitationById(inviteId);

        stageInvitationValidator.validateUserInvitePermission(invited, stageInvitation);

        stageInvitation.setStatus(StageInvitationStatus.ACCEPTED);

        Stage stage = stageInvitation.getStage();
        stage.getExecutors().add(invited);

        return stageInvitationMapper.toDto(stageInvitationRepository.save(stageInvitation));
    }

    @Override
    @Transactional
    public StageInvitationDto rejectInvitation(long userId, long inviteId, String reason) {

        TeamMember invited = findTeamMemberById(userId);
        StageInvitation stageInvitation = findStageInvitationById(inviteId);

        stageInvitationValidator.validateUserInvitePermission(invited, stageInvitation);

        stageInvitation.setStatus(StageInvitationStatus.REJECTED);
        stageInvitation.setDescription(reason);

        return stageInvitationMapper.toDto(stageInvitationRepository.save(stageInvitation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StageInvitationDto> getInvitationsWithFilters(StageInvitationFilterDto stageInvitationFilterDto) {

        Stream<StageInvitation> invitations = stageInvitationRepository.findAll().stream();

        return stageInvitationFilterService.applyAll(invitations, stageInvitationFilterDto)
                .map(stageInvitationMapper::toDto)
                .toList();
    }

    private StageInvitation findStageInvitationById(long stageInvitationId) {
        return stageInvitationRepository.findById(stageInvitationId)
                .orElseThrow(() -> new NotFoundException(String.format("Stage invitation doesn't exist by id: %s", stageInvitationId))
        );
    }

    private TeamMember findTeamMemberById(long teamMemberId) {
        return teamMemberRepository.findById(teamMemberId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Team member doesn't exist by id: %s", teamMemberId)));
    }
}
