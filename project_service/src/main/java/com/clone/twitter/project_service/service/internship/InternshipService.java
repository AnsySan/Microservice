package com.clone.twitter.project_service.service.internship;

import com.clone.twitter.project_service.dto.internship.InternshipDto;
import com.clone.twitter.project_service.dto.internship.InternshipFilterDto;
import com.clone.twitter.project_service.dto.internship.InternshipToCreateDto;
import com.clone.twitter.project_service.dto.internship.InternshipToUpdateDto;

import java.util.List;

public interface InternshipService {
    InternshipDto createInternship(long userId, InternshipToCreateDto internshipDto);

    InternshipDto updateInternship(long userId, long internshipId, InternshipToUpdateDto updatedInternshipDto);

    List<InternshipDto> getAllInternshipsByProjectId(long projectId, InternshipFilterDto filterDto);

    List<InternshipDto> getAllInternships(InternshipFilterDto filter);

    InternshipDto getInternshipById(long id);
}
