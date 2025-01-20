package com.clone.twitter.project_service.validation.internship;

import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.model.Internship;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.TeamRole;
import com.clone.twitter.project_service.repository.InternshipRepository;
import com.clone.twitter.project_service.repository.ProjectRepository;
import com.clone.twitter.project_service.repository.TeamMemberRepository;
import com.clone.twitter.project_service.validation.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InternshipValidatorTest {

    @Mock
    private InternshipRepository internshipRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private InternshipValidator internshipValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateFinishInternshipForIntern_success() {
        Internship internship = mock(Internship.class);
        TeamMember intern = mock(TeamMember.class);
        when(internship.getEndDate()).thenReturn(LocalDateTime.now().plusDays(10));

        internshipValidator.validateFinishInternshipForIntern(internship, intern);
    }

    @Test
    void validateFinishInternshipForIntern_afterEndDate_throwsException() {
        Internship internship = mock(Internship.class);
        TeamMember intern = mock(TeamMember.class);
        when(internship.getEndDate()).thenReturn(LocalDateTime.now().minusDays(10));

        DataValidationException exception = assertThrows(DataValidationException.class, () -> {
            internshipValidator.validateFinishInternshipForIntern(internship, intern);
        });

        assertEquals(String.format("Internship %d already finished.", internship.getId()), exception.getMessage());
    }

    @Test
    void validateRemoveInternFromInternship_success() {
        Internship internship = mock(Internship.class);
        TeamMember intern = mock(TeamMember.class);
        when(internship.getInterns()).thenReturn(List.of(intern));

        internshipValidator.validateRemoveInternFromInternship(internship, intern);
    }

    @Test
    void validateRemoveInternFromInternship_notInInternship_throwsException() {
        Internship internship = mock(Internship.class);
        TeamMember intern = mock(TeamMember.class);
        when(internship.getInterns()).thenReturn(Collections.emptyList());

        DataValidationException exception = assertThrows(DataValidationException.class, () -> {
            internshipValidator.validateRemoveInternFromInternship(internship, intern);
        });

        assertEquals(String.format("Intern with id %d is not in internship with id %d", intern.getId(), internship.getId()), exception.getMessage());
    }

    @Test
    void validateTeamMemberDontHaveThisRole_alreadyHasRole_throwsException() {
        TeamMember teamMember = mock(TeamMember.class);
        when(teamMember.getRoles()).thenReturn(List.of(TeamRole.DEVELOPER));

        DataValidationException exception = assertThrows(DataValidationException.class, () -> {
            internshipValidator.validateTeamMemberDontHaveThisRole(teamMember, TeamRole.DEVELOPER);
        });

        assertEquals(String.format("Team member %d already has role %s", teamMember.getId(), TeamRole.DEVELOPER.name()), exception.getMessage());
    }
}
