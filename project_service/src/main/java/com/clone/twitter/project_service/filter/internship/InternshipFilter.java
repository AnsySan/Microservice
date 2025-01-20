package com.clone.twitter.project_service.filter.internship;

import com.clone.twitter.project_service.dto.internship.InternshipFilterDto;
import com.clone.twitter.project_service.model.Internship;

import java.util.stream.Stream;

public interface InternshipFilter {

    boolean isAcceptable(InternshipFilterDto internshipFilterDto);

    Stream<Internship> applyFilter(Stream<Internship> internships, InternshipFilterDto internshipFilterDto);
}