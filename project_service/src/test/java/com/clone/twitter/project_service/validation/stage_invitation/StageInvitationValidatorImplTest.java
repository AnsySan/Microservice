package com.clone.twitter.project_service.validation.stage_invitation;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationCreateDto;
import com.clone.twitter.project_service.exceptions.NoAccessException;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.stage.Stage;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitation;
import com.clone.twitter.project_service.validation.stage.StageValidator;
import com.clone.twitter.project_service.validation.team_member.TeamMemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class StageInvitationValidatorImplTest {

    @Mock
    private TeamMemberValidator teamMemberValidator;
    @Mock
    private StageValidator stageValidator;

    @InjectMocks
    private StageInvitationValidatorImpl stageInvitationValidator;

    private final long stageId = 1L;
    private final long authorId = 2L;
    private final long invitedId = 3L;
    private final long invitationId = 4L;
    private StageInvitationCreateDto stageInvitationCreateDto;
    private TeamMember teamMember;
    private StageInvitation stageInvitation;

    @BeforeEach
    void setUp() {
        stageInvitationCreateDto = StageInvitationCreateDto.builder()
                .stageId(stageId)
                .authorId(authorId)
                .invitedId(invitedId)
                .build();

        teamMember = TeamMember.builder().id(invitedId).build();

        stageInvitation = StageInvitation.builder()
                .id(invitationId)
                .author(TeamMember.builder().id(authorId).build())
                .invited(teamMember)
                .stage(Stage.builder().stageId(stageId).build())
                .build();
    }

    @Test
    void validateUserInvitePermission() {
        assertDoesNotThrow(() -> stageInvitationValidator.validateUserInvitePermission(teamMember, stageInvitation));
    }

    @Test
    void validateUserInvitePermissionNoAccess() {
        stageInvitation.setInvited(new TeamMember());

        NoAccessException e = assertThrows(NoAccessException.class, () -> stageInvitationValidator.validateUserInvitePermission(teamMember, stageInvitation));
        assertEquals("User with id=" + invitedId + " does not have access to invite with id=" + invitationId, e.getMessage());
    }

    @Test
    void validateExistences() {
        assertDoesNotThrow(() -> stageInvitationValidator.validateExistences(stageInvitationCreateDto));

        InOrder inOrder = inOrder(teamMemberValidator, stageValidator);
        inOrder.verify(teamMemberValidator).validateExistence(authorId);
        inOrder.verify(teamMemberValidator).validateExistence(invitedId);
        inOrder.verify(stageValidator).validateExistence(stageId);
    }
}