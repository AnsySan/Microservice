package com.clone.twitter.project_service.service.internship;

import com.amazonaws.services.kms.model.NotFoundException;
import com.clone.twitter.project_service.dto.internship.InternshipDto;
import com.clone.twitter.project_service.dto.internship.InternshipFilterDto;
import com.clone.twitter.project_service.dto.internship.InternshipToCreateDto;
import com.clone.twitter.project_service.dto.internship.InternshipToUpdateDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.mapper.internship.InternshipMapper;
import com.clone.twitter.project_service.model.Internship;
import com.clone.twitter.project_service.model.InternshipStatus;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.TeamRole;
import com.clone.twitter.project_service.repository.InternshipRepository;
import com.clone.twitter.project_service.repository.TeamMemberRepository;
import com.clone.twitter.project_service.service.internship.filter.InternshipFilterService;
import com.clone.twitter.project_service.validation.internship.InternshipValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.clone.twitter.project_service.model.TaskStatus.DONE;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternshipServiceImpl implements InternshipService {
    private final InternshipRepository internshipRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final InternshipValidator validator;
    private final InternshipMapper internshipMapper;
    private final InternshipFilterService internshipFilterService;

    @Override
    @Transactional
    public InternshipDto createInternship(long userId, InternshipToCreateDto internshipDto) {
        Internship internship = internshipMapper.toEntity(internshipDto);
        internship.setCreatedBy(userId);

        internship.setStatus(InternshipStatus.IN_PROGRESS);

        validator.validateCreateInternship(userId, internshipDto);

        internshipRepository.save(internship);
        log.info("Created internship {}", internship.getId());
        return internshipMapper.toDto(internship);
    }

    @Override
    @Transactional
    public InternshipDto updateInternship(long userId, long internshipId, InternshipToUpdateDto internshipDto) {
        Internship internship = findById(internshipId);
        if (InternshipStatus.valueOf(String.valueOf(internship.getStatus())).equals(InternshipStatus.COMPLETED)) {
            throw new DataValidationException("Completed internship cannot be updated");
        }

        validator.validateUpdateInternship(internship, internshipDto);

        Set<Long> currentInternIds = internship.getInterns().stream()
                .map(TeamMember::getId)
                .collect(Collectors.toSet());

        if (!currentInternIds.equals(Set.copyOf(internshipDto.getInternsId()))) {
            throw new DataValidationException("Cannot change interns after the internship has started");
        }

        if (InternshipStatus.valueOf(internshipDto.getStatus()).equals(InternshipStatus.COMPLETED)) {
            completeInternship(internship);
        }

        internshipMapper.update(internshipDto, internship);
        internship.setUpdatedBy(userId);
        internshipRepository.save(internship);
        log.info("Updated internship {}", internship.getId());
        return internshipMapper.toDto(internship);
    }

    @Override
    @Transactional(readOnly = true)
    public InternshipDto getInternshipById(long internshipId) {
        return internshipMapper.toDto(findById(internshipId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipDto> getAllInternshipsByProjectId(long projectId, InternshipFilterDto filterDto) {
        return internshipFilterService.applyFilters(internshipRepository.findByProjectId(projectId).stream(), filterDto)
                .map(internshipMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternshipDto> getAllInternships(InternshipFilterDto filterDto) {
        return internshipFilterService.applyFilters(internshipRepository.findAll().stream(), filterDto)
                .map(internshipMapper::toDto)
                .toList();
    }

    private void completeInternship(Internship internship) {
        Iterator<TeamMember> iterator = internship.getInterns().iterator();
        while (iterator.hasNext()) {
            TeamMember intern = iterator.next();
            if (allTasksDone(intern)) {
                finishInternshipForIntern(internship.getId(), intern.getId(), TeamRole.DEVELOPER);
                iterator.remove();
            } else {
                removeInternFromInternship(internship.getId(), intern.getId());
                iterator.remove();
            }
        }
    }

    private boolean allTasksDone(TeamMember intern) {
        return intern.getStages().stream()
                .flatMap(stage -> stage.getTasks().stream())
                .allMatch(task -> task.getStatus().equals(DONE));
    }

    private void finishInternshipForIntern(long internshipId, long internId, TeamRole teamRole) {
        Internship internship = findById(internshipId);
        TeamMember intern = getInternFromInternship(internship, internId);
        validator.validateFinishInternshipForIntern(internship, intern);
        changeInternRole(intern, teamRole);
        log.info("Intern {} finished internship {} and got teamRole {}", internId, internshipId, teamRole);
    }


    private void removeInternFromInternship(long internshipId, long internId) {
        Internship internship = findById(internshipId);
        TeamMember intern = getInternFromInternship(internship, internId);
        validator.validateRemoveInternFromInternship(internship, intern);
        log.info("Removed intern {} from internship {}", internId, internshipId);
    }


    private void changeInternRole(TeamMember teamMember, TeamRole newRole) {
        if (teamMember.getRoles() == null) {
            teamMember.setRoles(new ArrayList<>());
        }

        teamMember.getRoles().remove(newRole);
        teamMember.getRoles().add(newRole);
        teamMemberRepository.save(teamMember);
        log.info("Changed team member {} role from intern to {}", teamMember.getId(), newRole);
    }

    private Internship findById(long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Internship with id %d not found", id)));
    }

    private TeamMember getInternFromInternship(Internship internship, long internId) {
        return internship.getInterns().stream()
                .filter(teamMember -> teamMember.getId().equals(internId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        String.format("Intern with id %d not found in the internship %d", internId, internship.getId())));
    }
}
