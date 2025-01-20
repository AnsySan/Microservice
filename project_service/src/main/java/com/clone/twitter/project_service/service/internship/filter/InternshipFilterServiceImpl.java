package com.clone.twitter.project_service.service.internship.filter;

import com.clone.twitter.project_service.dto.internship.InternshipFilterDto;
import com.clone.twitter.project_service.filter.internship.InternshipFilter;
import com.clone.twitter.project_service.model.Internship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InternshipFilterServiceImpl implements InternshipFilterService {
    private final List<InternshipFilter> internshipFilters;

    @Override
    public Stream<Internship> applyFilters(Stream<Internship> internships, InternshipFilterDto internshipFilterDto) {
        if (internshipFilterDto != null) {
            for (InternshipFilter internshipFilter : internshipFilters) {
                if (internshipFilter.isAcceptable(internshipFilterDto)) {
                    internships = internshipFilter.applyFilter(internships, internshipFilterDto);
                }
            }
        }

        return internships;
    }
}
