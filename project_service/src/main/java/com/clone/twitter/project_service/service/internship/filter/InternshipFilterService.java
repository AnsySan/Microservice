package com.clone.twitter.project_service.service.internship.filter;

import com.clone.twitter.project_service.dto.internship.InternshipFilterDto;
import com.clone.twitter.project_service.model.Internship;

import java.util.stream.Stream;

public interface InternshipFilterService {
    Stream<Internship> applyFilters(Stream<Internship> internships, InternshipFilterDto userFilterDto);
}