package com.clone.twitter.project_service.filter.internship;

import com.clone.twitter.project_service.dto.internship.InternshipFilterDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.model.Internship;
import com.clone.twitter.project_service.model.InternshipStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class StatusFilter implements InternshipFilter {

    @Override
    public boolean isAcceptable(InternshipFilterDto internshipFilterDto) {
        return internshipFilterDto.getStatus() != null;
    }

    @Override
    public Stream<Internship> applyFilter(Stream<Internship> internships, InternshipFilterDto internshipFilterDto) {
        try {
            InternshipStatus filterStatus = internshipFilterDto.getStatus();
            return internships.filter(internship -> internship.getStatus().equals(filterStatus));
        } catch (IllegalArgumentException e) {
            throw new DataValidationException("Incorrect filter value");
        }
    }
}
